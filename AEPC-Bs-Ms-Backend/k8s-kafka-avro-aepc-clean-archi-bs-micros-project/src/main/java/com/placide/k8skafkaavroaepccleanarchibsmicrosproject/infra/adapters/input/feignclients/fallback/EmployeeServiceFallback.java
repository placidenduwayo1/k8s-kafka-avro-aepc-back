package com.placide.k8skafkaavroaepccleanarchibsmicrosproject.infra.adapters.input.feignclients.fallback;

import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.infra.adapters.input.feignclients.models.EmployeeModel;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.infra.adapters.input.feignclients.proxies.EmployeeServiceProxy;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.exceptions.Msg.REMOTE_EMPLOYEE_API_UNREACHABLE;

@Component
public class EmployeeServiceFallback implements EmployeeServiceProxy {
    @Override
    public Optional<EmployeeModel> loadRemoteApiGetEmployee(String employeeId) {
        return Optional.of( EmployeeModel.builder()
                .employeeId(REMOTE_EMPLOYEE_API_UNREACHABLE)
                .firstname(REMOTE_EMPLOYEE_API_UNREACHABLE)
                .lastname(REMOTE_EMPLOYEE_API_UNREACHABLE)
                .email(REMOTE_EMPLOYEE_API_UNREACHABLE)
                .hireDate(REMOTE_EMPLOYEE_API_UNREACHABLE)
                .state(REMOTE_EMPLOYEE_API_UNREACHABLE)
                .type(REMOTE_EMPLOYEE_API_UNREACHABLE)
                .build());
    }
}
