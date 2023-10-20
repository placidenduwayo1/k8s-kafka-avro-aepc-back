package com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.ports.input;

import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.beans.employee.Employee;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.exceptions.RemoteEmployeeApiException;


public interface InputRemoteApiEmployeeService {
    Employee getRemoteEmployeeAPI(String employeeId) throws RemoteEmployeeApiException;
}
