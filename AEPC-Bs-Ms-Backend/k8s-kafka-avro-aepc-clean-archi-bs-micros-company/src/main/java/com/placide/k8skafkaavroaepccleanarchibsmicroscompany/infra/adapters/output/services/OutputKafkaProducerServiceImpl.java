package com.placide.k8skafkaavroaepccleanarchibsmicroscompany.infra.adapters.output.services;

import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.avrobean.CompanyAvro;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.ports.output.OutputKafkaProducerService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class OutputKafkaProducerServiceImpl implements OutputKafkaProducerService {
    private final KafkaTemplate<String, CompanyAvro> companyKafkaTemplate;

    public OutputKafkaProducerServiceImpl(KafkaTemplate<String, CompanyAvro> companyKafkaTemplate) {
        this.companyKafkaTemplate = companyKafkaTemplate;
    }

    private Message<?> buildMessage(CompanyAvro companyAvro, String topic){
        return MessageBuilder.withPayload(companyAvro)
                .setHeader(KafkaHeaders.TOPIC,topic)
                .build();
    }

    @Override
    public CompanyAvro produceKafkaEventCompanyCreate(CompanyAvro companyAvro) {
        Message<?> message = buildMessage(companyAvro,"avro-companies-created");
        companyKafkaTemplate.send(message);
        return companyAvro;
    }

    @Override
    public CompanyAvro produceKafkaEventCompanyDelete(CompanyAvro companyAvro) {
        Message<?> message =buildMessage(companyAvro,"avro-companies-deleted");
        companyKafkaTemplate.send(message);
        return companyAvro;
    }

    @Override
    public CompanyAvro produceKafkaEventCompanyEdit(CompanyAvro companyAvro) {
        Message<?> message = buildMessage(companyAvro,"avro-companies-edited");
        companyKafkaTemplate.send(message);
        return companyAvro;
    }
}
