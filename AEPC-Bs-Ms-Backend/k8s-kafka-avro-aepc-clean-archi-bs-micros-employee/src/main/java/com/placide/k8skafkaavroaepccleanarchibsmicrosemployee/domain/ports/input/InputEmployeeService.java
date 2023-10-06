package com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.ports.input;

import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.beans.employee.Employee;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.exceptions.*;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.infra.adapters.output.models.EmployeeDto;

import java.util.List;
import java.util.Optional;

public interface InputEmployeeService {
    Employee produceKafkaEventEmployeeCreate(EmployeeDto employeeDto) throws
            EmployeeTypeInvalidException, EmployeeEmptyFieldsException,
            EmployeeStateInvalidException, RemoteApiAddressNotLoadedException, EmployeeAlreadyExistsException;
    Employee createEmployee(Employee employee) throws
            EmployeeAlreadyExistsException, EmployeeEmptyFieldsException,
            EmployeeTypeInvalidException, EmployeeStateInvalidException,
            RemoteApiAddressNotLoadedException;
    Optional<Employee> getEmployeeById(String employeeId) throws EmployeeNotFoundException;
    List<Employee> loadEmployeeByInfo(String firstname, String lastname, String state, String type, String addressId);
    List<Employee> loadAllEmployees();
    Employee produceKafkaEventEmployeeDelete(String employeeId) throws EmployeeNotFoundException, RemoteApiAddressNotLoadedException;
    String deleteEmployee(String employeeId) throws EmployeeNotFoundException, RemoteApiAddressNotLoadedException;
    Employee produceKafkaEventEmployeeEdit(EmployeeDto payload, String employeeId) throws
            RemoteApiAddressNotLoadedException, EmployeeNotFoundException, EmployeeTypeInvalidException, EmployeeEmptyFieldsException, EmployeeStateInvalidException;
    Employee editEmployee(Employee payload) throws RemoteApiAddressNotLoadedException;
    List<Employee> loadEmployeesByRemoteAddress(String addressId) throws RemoteApiAddressNotLoadedException;
}
