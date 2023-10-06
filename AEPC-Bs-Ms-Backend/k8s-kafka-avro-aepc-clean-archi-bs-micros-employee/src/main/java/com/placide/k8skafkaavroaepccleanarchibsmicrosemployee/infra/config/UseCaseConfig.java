package com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.infra.config;

import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.ports.output.OutputRemoteAddressService;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.ports.output.OutputEmployeeService;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.usecase.UseCase;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.infra.adapters.output.service.OutputKafkaProducerEmployeeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class UseCaseConfig {
    @Bean
    public UseCase configUseCase(
            @Autowired OutputKafkaProducerEmployeeServiceImpl kafkaProducer,
            @Autowired OutputEmployeeService employeeService,
            @Autowired OutputRemoteAddressService addressService) {
        return new UseCase(kafkaProducer, employeeService, addressService);

    }
}
