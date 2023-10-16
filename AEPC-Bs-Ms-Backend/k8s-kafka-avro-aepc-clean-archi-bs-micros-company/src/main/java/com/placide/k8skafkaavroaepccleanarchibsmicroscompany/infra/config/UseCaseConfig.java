package com.placide.k8skafkaavroaepccleanarchibsmicroscompany.infra.config;

import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.ports.output.OutputCompanyService;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.ports.output.OutputRemoteAddressService;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.usecase.UseCase;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.infra.adapters.output.services.OutputKafkaProducerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class UseCaseConfig {
    @Bean
    UseCase configCompanyUseCase(@Autowired OutputKafkaProducerServiceImpl kafkaProducer,
                                 @Autowired OutputCompanyService companyService,
                                 @Autowired OutputRemoteAddressService outputRemoteAddressService){
        return new UseCase(kafkaProducer, companyService, outputRemoteAddressService);
    }
}
