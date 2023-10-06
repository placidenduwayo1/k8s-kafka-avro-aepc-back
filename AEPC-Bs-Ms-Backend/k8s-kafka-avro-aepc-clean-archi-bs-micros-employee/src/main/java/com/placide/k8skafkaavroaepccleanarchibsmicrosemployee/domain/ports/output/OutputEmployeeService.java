package com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.ports.output;

import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.avrobeans.EmployeeAvro;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.beans.employee.Employee;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.exceptions.EmployeeNotFoundException;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.exceptions.RemoteApiAddressNotLoadedException;

import java.util.List;
import java.util.Optional;

public interface OutputEmployeeService {
    Employee consumeKafkaEventEmployeeCreate(EmployeeAvro employeeAvro, String topic) throws RemoteApiAddressNotLoadedException;
    Employee saveEmployee(Employee employee) throws RemoteApiAddressNotLoadedException;
    Optional<Employee> getEmployeeById(String employeeId) throws EmployeeNotFoundException;
    List<Employee> loadEmployeesByRemoteAddress(String addressId) throws RemoteApiAddressNotLoadedException;
    List<Employee> loadEmployeeByInfo(String firstname, String lastname, String state, String type, String addressId);
    List<Employee> loadAllEmployees();
    Employee consumeKafkaEventEmployeeDelete(EmployeeAvro employeeAvro, String topic) throws RemoteApiAddressNotLoadedException;
    String deleteEmployee(String employeeId) throws EmployeeNotFoundException, RemoteApiAddressNotLoadedException;

    Employee consumeKafkaEventEmployeeEdit(EmployeeAvro employeeAvro, String topic) throws RemoteApiAddressNotLoadedException;
    Employee editEmployee(Employee employee) throws RemoteApiAddressNotLoadedException;
}
