package com.placide.k8skafkaavroaepccleanarchibsmicrosproject.infra.adapters.output.services;

import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.avrobeans.ProjectAvro;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.ports.output.OutputKafkaProducerProjectService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class OutputKafkaProducerProjectServiceImpl implements OutputKafkaProducerProjectService {
    private final KafkaTemplate<String, ProjectAvro> kafkaTemplate;

    public OutputKafkaProducerProjectServiceImpl(KafkaTemplate<String, ProjectAvro> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    private Message<?> buildKfakaMessage(ProjectAvro projectAvro, String topic){
        return MessageBuilder.withPayload(projectAvro)
                .setHeader(KafkaHeaders.TOPIC,topic)
                .build();
    }
    @Override
    public ProjectAvro produceKafkaEventProjectCreate(ProjectAvro projectAvro) {
        kafkaTemplate.send(buildKfakaMessage(projectAvro,"avro-projects-created"));
        return projectAvro;
    }

    @Override
    public ProjectAvro produceKafkaEventProjectDelete(ProjectAvro projectAvro) {
        kafkaTemplate.send(buildKfakaMessage(projectAvro,"avro-projects-deleted"));
        return projectAvro;
    }

    @Override
    public ProjectAvro produceKafkaEventProjectEdit(ProjectAvro projectAvro){
        kafkaTemplate.send(buildKfakaMessage(projectAvro,"avro-projects-edited"));
        return projectAvro;
    }
}
