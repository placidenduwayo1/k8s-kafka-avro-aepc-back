package com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.infra.adapters.output.service;

import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.avrobeans.EmployeeAvro;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.beans.address.Address;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.beans.employee.Employee;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.exceptions.EmployeeNotFoundException;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.exceptions.RemoteApiAddressNotLoadedException;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.ports.output.OutputRemoteAddressService;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.ports.output.OutputEmployeeService;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.infra.adapters.output.mapper.AddressMapper;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.infra.adapters.output.mapper.EmployeeMapper;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.infra.adapters.input.feignclients.models.AddressModel;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.infra.adapters.input.feignclients.proxy.AddressServiceProxy;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.infra.adapters.output.models.EmployeeModel;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.infra.adapters.output.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Slf4j
@Service
public class OutputEmployeeServiceImpl implements  OutputEmployeeService, OutputRemoteAddressService {
    private final EmployeeRepository repository;
    private final AddressServiceProxy addressProxy;

    public OutputEmployeeServiceImpl(EmployeeRepository repository,
                                     @Qualifier(value="address-service-proxy") AddressServiceProxy addressProxy) {
        this.repository = repository;
        this.addressProxy = addressProxy;
    }

    @Override
    @KafkaListener(topics = "avro-employees-created",groupId = "employee-group-id")
    public Employee consumeKafkaEventEmployeeCreate(@Payload EmployeeAvro employeeAvro,
                                                    @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        Employee employee = EmployeeMapper.fromAvroToBean(employeeAvro);
       log.info("employee to add: <{}> consumed from topic: <{}>",employee,topic);
        return employee;
    }

    @Override
    public Employee saveEmployee(Employee employee) {
        EmployeeAvro employeeAvro = EmployeeMapper.fromBeanToAvro(employee);
        Employee consumed = consumeKafkaEventEmployeeCreate(employeeAvro,"avro-employees-created");
        EmployeeModel savedEmployee = repository.save(EmployeeMapper.toModel(consumed));
        return EmployeeMapper.toBean(savedEmployee);
    }

    private List<Employee> employeeModelToBean(List<EmployeeModel> models){
        return models.stream()
                .map(EmployeeMapper::toBean)
                .toList();
    }
    @Override
    public Optional<Employee> getEmployeeById(String employeeId) throws EmployeeNotFoundException {
        EmployeeModel model = repository.findById(employeeId).orElseThrow(
                EmployeeNotFoundException::new);
        return Optional.of(EmployeeMapper.toBean(model));
    }

    @Override
    public List<Employee> loadEmployeesByRemoteAddress(String addressId) throws RemoteApiAddressNotLoadedException {
        AddressModel model= addressProxy.loadRemoteApiGetAddressById(addressId).orElseThrow(RemoteApiAddressNotLoadedException::new);
        List<EmployeeModel> models = repository.findByAddressId(model.getAddressId());
        return employeeModelToBean(models);
    }

    @Override
    public List<Employee> loadEmployeeByInfo(String firstname, String lastname, String state, String type,String addressId) {
        return employeeModelToBean(repository
                .findByFirstnameAndLastnameAndStateAndTypeAndAddressId(firstname,lastname,state,type,addressId)
        );
    }

    @Override
    public List<Employee> loadAllEmployees() {
        return employeeModelToBean(repository.findAll());
    }

    @Override
    @KafkaListener(topics = "avro-employees-deleted", groupId = "-employee-group-id}")
    public Employee consumeKafkaEventEmployeeDelete(@Payload EmployeeAvro employeeAvro,
                                                    @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        Employee employee = EmployeeMapper.fromAvroToBean(employeeAvro);
        log.info("employee to delete:<{}> consumed from topic:<{}>",employee,topic);
       return employee;
    }

    @Override
    public String deleteEmployee(String employeeId) throws EmployeeNotFoundException {
        Employee bean = getEmployeeById(employeeId).orElseThrow(EmployeeNotFoundException::new);
        repository.deleteById(bean.getEmployeeId());
        return "employee <"+bean+"> is deleted";
    }

    @Override
    @KafkaListener(topics = "avro-employee-edited", groupId = "employee-group-id")
    public Employee consumeKafkaEventEmployeeEdit(@Payload EmployeeAvro employeeAvro,
                                                  @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        Employee employee = EmployeeMapper.fromAvroToBean(employeeAvro);
        log.info("employee to update :<{}> consumed from topic:<{}>",employee, topic);
        return employee;
    }

    @Override
    public Employee editEmployee(Employee employee) {
        EmployeeAvro avro = EmployeeMapper.fromBeanToAvro(employee);
        Employee consumed = consumeKafkaEventEmployeeEdit(avro,"avro-employee-edited");
        EmployeeModel mapped = EmployeeMapper.toModel(consumed);
        return EmployeeMapper.toBean(repository.save(mapped));
    }

    @Override
    public Optional<Address> getRemoteAddressById(String addressId) throws RemoteApiAddressNotLoadedException {
        return Optional.of(AddressMapper.toBean(addressProxy.loadRemoteApiGetAddressById(addressId)
                .orElseThrow(RemoteApiAddressNotLoadedException::new)));
    }

    @Override
    public List<Address> loadAllRemoteAddresses() {
        List<AddressModel> models = addressProxy.loadRemoteAddressIpiGetAllAddresses();
        return models.stream()
                .map(AddressMapper::toBean)
                .toList();
    }
}
