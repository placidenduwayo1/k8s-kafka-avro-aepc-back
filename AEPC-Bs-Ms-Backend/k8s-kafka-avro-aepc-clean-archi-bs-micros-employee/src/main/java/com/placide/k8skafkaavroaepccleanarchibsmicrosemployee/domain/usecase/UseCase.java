package com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.usecase;

import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.avrobeans.EmployeeAvro;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.beans.address.Address;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.beans.employee.Employee;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.exceptions.*;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.ports.input.RemoteInputAddressService;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.ports.input.InputEmployeeService;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.ports.output.OutputKafkaProducerEmployeeService;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.ports.output.OutputRemoteAddressService;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.ports.output.OutputEmployeeService;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.infra.adapters.output.mapper.EmployeeMapper;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.infra.adapters.output.models.EmployeeDto;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UseCase implements InputEmployeeService, RemoteInputAddressService {
    private final OutputKafkaProducerEmployeeService outputKafkaProducerEmployeeService;
    private final OutputEmployeeService outputEmployeeService;
    private final OutputRemoteAddressService outputRemoteAddressService;


    public UseCase(OutputKafkaProducerEmployeeService outputKafkaProducerEmployeeService,
                   OutputEmployeeService outputEmployeeService,
                   OutputRemoteAddressService outputRemoteAddressService) {
        this.outputKafkaProducerEmployeeService = outputKafkaProducerEmployeeService;
        this.outputEmployeeService = outputEmployeeService;
        this.outputRemoteAddressService = outputRemoteAddressService;
    }

    private void checkEmployeeValidity(EmployeeDto employeeDto) throws
            EmployeeEmptyFieldsException, EmployeeStateInvalidException,
            EmployeeTypeInvalidException, RemoteApiAddressNotLoadedException {
        if (!Validator.isValidEmployee(
                employeeDto.getFirstname(), employeeDto.getLastname(),
                employeeDto.getState(), employeeDto.getType(),
                employeeDto.getAddressId())) {
            throw new EmployeeEmptyFieldsException();
        } else if (!Validator.checkStateValidity(employeeDto.getState())) {
            throw new EmployeeStateInvalidException();
        } else if (!Validator.checkTypeValidity(employeeDto.getType())) {
            throw new EmployeeTypeInvalidException();
        }
        Address address = getRemoteAddressById(employeeDto.getAddressId());
        if (Validator.remoteAddressApiUnreachable(address.getAddressId())) {
            throw new RemoteApiAddressNotLoadedException(address.toString());
        }
    }

    private void checkEmployeeAlreadyExist(EmployeeDto dto) throws EmployeeAlreadyExistsException {
        if (!loadEmployeeByInfo(dto.getFirstname(), dto.getLastname(), dto.getState(), dto.getType(),
                dto.getAddressId()).isEmpty()) {
            throw new EmployeeAlreadyExistsException();
        }
    }

    private void setEmployeeDependency(Employee employee, String addressId) throws RemoteApiAddressNotLoadedException {
        Address address = getRemoteAddressById(addressId);
        employee.setAddressId(addressId);
        employee.setAddress(address);
    }

    //kafka producer employee create, edit and delete events
    @Override
    public Employee produceKafkaEventEmployeeCreate(EmployeeDto employeeDto) throws
            EmployeeTypeInvalidException, EmployeeEmptyFieldsException,
            EmployeeStateInvalidException, RemoteApiAddressNotLoadedException,
            EmployeeAlreadyExistsException {

        Validator.formatter(employeeDto);
        checkEmployeeValidity(employeeDto);
        checkEmployeeAlreadyExist(employeeDto);
        Employee employee = EmployeeMapper.fromDto(employeeDto);
        employee.setEmployeeId(UUID.randomUUID().toString());
        employee.setHireDate(Timestamp.from(Instant.now()).toString());
        employee.setEmail(Validator.setEmail(employee.getFirstname(), employee.getLastname()));
        setEmployeeDependency(employee,employeeDto.getAddressId());
        EmployeeAvro employeeAvro = EmployeeMapper.fromBeanToAvro(employee);
        return EmployeeMapper.fromAvroToBean(outputKafkaProducerEmployeeService.produceKafkaEventEmployeeCreate(employeeAvro));
    }

    @Override
    public Employee createEmployee(Employee employee) throws RemoteApiAddressNotLoadedException {
        Employee saved= outputEmployeeService.saveEmployee(employee);
        setEmployeeDependency(saved, saved.getAddressId());
        return saved;
    }

    @Override
    public Employee produceKafkaEventEmployeeDelete(String employeeId) throws EmployeeNotFoundException, RemoteApiAddressNotLoadedException {
        Employee employee = getEmployeeById(employeeId).orElseThrow(EmployeeNotFoundException::new);
        setEmployeeDependency(employee,employee.getAddressId());
        EmployeeAvro employeeAvro = EmployeeMapper.fromBeanToAvro(employee);
        return EmployeeMapper.fromAvroToBean(outputKafkaProducerEmployeeService.produceKafkaEventEmployeeDelete(employeeAvro));
    }

    @Override
    public String deleteEmployee(String employeeId) throws EmployeeNotFoundException, RemoteApiAddressNotLoadedException {
        Employee employee = getEmployeeById(employeeId).orElseThrow(EmployeeNotFoundException::new);
        outputEmployeeService.deleteEmployee(employee.getEmployeeId());
        return "Employee" + employee + "successfully deleted";
    }

    @Override
    public Employee produceKafkaEventEmployeeEdit(EmployeeDto employeeDto, String employeeId) throws
            RemoteApiAddressNotLoadedException, EmployeeNotFoundException, EmployeeTypeInvalidException, EmployeeEmptyFieldsException,
            EmployeeStateInvalidException {
        Validator.formatter(employeeDto);
        checkEmployeeValidity(employeeDto);

        Employee employee = getEmployeeById(employeeId).orElseThrow(EmployeeNotFoundException::new);
        employee.setFirstname(employeeDto.getFirstname());
        employee.setLastname(employeeDto.getLastname());
        employee.setEmail(Validator.setEmail(employeeDto.getFirstname(), employeeDto.getLastname()));
        employee.setState(employeeDto.getState());
        employee.setType(employeeDto.getType());
        setEmployeeDependency(employee,employeeDto.getAddressId());
        EmployeeAvro employeeAvro = EmployeeMapper.fromBeanToAvro(employee);
        return EmployeeMapper.fromAvroToBean(outputKafkaProducerEmployeeService.produceKafkaEventEmployeeEdit(employeeAvro));
    }

    @Override
    public Employee editEmployee(Employee payload) throws RemoteApiAddressNotLoadedException {
        Employee updated = outputEmployeeService.editEmployee(payload);
        setEmployeeDependency(updated, updated.getAddressId());
        return updated;
    }

    @Override
    public List<Employee> loadEmployeesByRemoteAddress(String addressId) throws RemoteApiAddressNotLoadedException {
        Address address = outputRemoteAddressService.getRemoteAddressById(addressId);
        List<Employee> employees = outputEmployeeService.loadEmployeesByRemoteAddress(address.getAddressId());
        employees.forEach(employee -> {
            try {
                setEmployeeDependency(employee, addressId);
            } catch (RemoteApiAddressNotLoadedException e) {
                e.getMessage();
            }
        });
        return employees;
    }

    @Override
    public Optional<Employee> getEmployeeById(String employeeId) throws EmployeeNotFoundException, RemoteApiAddressNotLoadedException {
       Employee employee = outputEmployeeService.getEmployeeById(employeeId).orElseThrow(
                EmployeeNotFoundException::new);
       setEmployeeDependency(employee, employee.getAddressId());
       return Optional.of(employee);
    }

    @Override
    public List<Employee> loadEmployeeByInfo(String firstname, String lastname, String state, String type, String addressId) {
        List<Employee> employees = outputEmployeeService.loadEmployeeByInfo(firstname, lastname, state, type, addressId);
        employees.forEach(employee -> {
            try {
                setEmployeeDependency(employee, employee.getAddressId());
            } catch (RemoteApiAddressNotLoadedException e) {
                e.getMessage();
            }
        });

        return employees;
    }

    @Override
    public List<Employee> loadAllEmployees() {
        List<Employee> employees = outputEmployeeService.loadAllEmployees();
        employees.forEach(employee -> {
            try {
                setEmployeeDependency(employee, employee.getAddressId());
            } catch (RemoteApiAddressNotLoadedException e) {
                e.getMessage();
            }
        });
        return employees;
    }

    @Override
    public Address getRemoteAddressById(String addressId) throws RemoteApiAddressNotLoadedException {
        return outputRemoteAddressService.getRemoteAddressById(addressId);
    }

    @Override
    public List<Address> loadRemoteAllAddresses() {
        return outputRemoteAddressService.loadAllRemoteAddresses();
    }
}
