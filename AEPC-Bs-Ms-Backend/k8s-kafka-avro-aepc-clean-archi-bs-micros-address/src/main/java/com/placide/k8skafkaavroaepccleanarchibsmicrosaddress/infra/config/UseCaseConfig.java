package com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.infra.config;

import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.domain.ports.output.OutputKafkaProducerAddressService;
import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.domain.ports.output.OutputAddressService;
import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.domain.usecase.UseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class UseCaseConfig {
    @Bean
    UseCase configInputAddressService(
            @Autowired OutputKafkaProducerAddressService kafkaProducer,
            @Autowired OutputAddressService outAddressService){
        return new UseCase(kafkaProducer, outAddressService);
    }
}
