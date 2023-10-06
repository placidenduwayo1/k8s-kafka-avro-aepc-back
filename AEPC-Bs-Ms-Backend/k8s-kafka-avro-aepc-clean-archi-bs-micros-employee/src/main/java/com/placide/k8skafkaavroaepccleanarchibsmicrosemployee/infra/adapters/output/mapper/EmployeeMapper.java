package com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.infra.adapters.output.mapper;

import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.avrobeans.Address;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.avrobeans.EmployeeAvro;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.beans.employee.Employee;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.infra.adapters.output.models.EmployeeDto;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.infra.adapters.output.models.EmployeeModel;
import org.springframework.beans.BeanUtils;

public class EmployeeMapper {

    private EmployeeMapper(){}
    public static Employee toBean(EmployeeModel model){
        Employee employee = new Employee();
        BeanUtils.copyProperties(model,employee);
        return employee;
    }
    public static EmployeeModel toModel(Employee bean){
        EmployeeModel model = new EmployeeModel();
        BeanUtils.copyProperties(bean, model);
        return model;
    }

    public static Employee fromDto(EmployeeDto dto){
        Employee bean = new Employee();
        BeanUtils.copyProperties(dto,bean);
        return bean;
    }

    public static EmployeeDto fromBeanToDto(Employee bean) {
        EmployeeDto dto = new EmployeeDto();
        BeanUtils.copyProperties(bean,dto);
        return dto;
    }

    public static EmployeeAvro fromBeanToAvro(Employee employee) {
        Address addressAvro = Address.newBuilder()
                .setAddressId(employee.getAddressId())
                .setNum(employee.getAddress().getNum())
                .setStreet(employee.getAddress().getStreet())
                .setPoBox(employee.getAddress().getPoBox())
                .setCity(employee.getAddress().getCity())
                .setCountry(employee.getAddress().getCountry())
                .build();

        return EmployeeAvro.newBuilder()
                .setEmployeeId(employee.getEmployeeId())
                .setFirstname(employee.getFirstname())
                .setLastname(employee.getLastname())
                .setEmail(employee.getEmail())
                .setHireDate(employee.getHireDate())
                .setState(employee.getState())
                .setType(employee.getType())
                .setAddressId(employee.getAddressId())
                .setAddress(addressAvro)
                .build();
    }

    public static Employee fromAvroToBean(EmployeeAvro employeeAvro){
        com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.beans.address.Address address =
                new com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.beans.address.Address(
                        employeeAvro.getAddressId(),
                        employeeAvro.getAddress().getNum(),
                        employeeAvro.getAddress().getStreet(),
                        employeeAvro.getAddress().getPoBox(),
                        employeeAvro.getAddress().getCity(),
                        employeeAvro.getAddress().getCountry()
                );

        return new Employee(employeeAvro.getEmployeeId(),
                employeeAvro.getFirstname(),
                employeeAvro.getLastname(),
                employeeAvro.getEmail(),
                employeeAvro.getHireDate(),
                employeeAvro.getState(),
                employeeAvro.getType(),
                employeeAvro.getAddressId(),
                address);
    }
}
