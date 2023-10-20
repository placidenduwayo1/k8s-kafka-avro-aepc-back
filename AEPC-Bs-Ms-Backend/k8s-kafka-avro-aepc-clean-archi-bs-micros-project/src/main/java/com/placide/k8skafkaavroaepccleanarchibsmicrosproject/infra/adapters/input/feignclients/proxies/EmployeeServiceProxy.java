package com.placide.k8skafkaavroaepccleanarchibsmicrosproject.infra.adapters.input.feignclients.proxies;

import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.infra.adapters.input.feignclients.fallback.EmployeeServiceFallback;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.infra.adapters.input.feignclients.models.EmployeeModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "k8s-kafka-avro-aepc-bs-ms-employee", fallback = EmployeeServiceFallback.class)
@Qualifier(value = "employee-service-proxy")
public interface EmployeeServiceProxy {
    @GetMapping("/employees/{employeeId}")
   EmployeeModel loadRemoteApiGetEmployee(@PathVariable(name = "employeeId") String employeeId);
}
