package com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.infra.adapters.output.service;

import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.avrobeans.EmployeeAvro;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.ports.output.OutputKafkaProducerEmployeeService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OutputKafkaProducerEmployeeServiceImpl implements OutputKafkaProducerEmployeeService {
    private final KafkaTemplate<String, EmployeeAvro> employeeKafkaTemplate;
    private static final List<String> TOPICS= List.of("avro-employees-created","avro-employees-deleted","avro-employees-edited");

    public OutputKafkaProducerEmployeeServiceImpl(KafkaTemplate<String, EmployeeAvro> employeeKafkaTemplate) {
        this.employeeKafkaTemplate = employeeKafkaTemplate;
    }

    private Message<?> buildKafkaMessage(EmployeeAvro payload, String topic) {
        return MessageBuilder.withPayload(payload)
                .setHeader(KafkaHeaders.TOPIC,topic)
                .build();
    }

    @Override
    public EmployeeAvro produceKafkaEventEmployeeCreate(EmployeeAvro employeeAvro) {
        Message<?> payload = buildKafkaMessage(employeeAvro,TOPICS.get(0));
        employeeKafkaTemplate.send(payload);
        return employeeAvro;
    }

    @Override
    public EmployeeAvro produceKafkaEventEmployeeDelete(EmployeeAvro employeeAvro){
        Message<?> payload = buildKafkaMessage(employeeAvro, TOPICS.get(1));
        employeeKafkaTemplate.send(payload);
        return employeeAvro;
    }

    @Override
    public EmployeeAvro produceKafkaEventEmployeeEdit(EmployeeAvro employeeAvro) {
        Message<?> payload = buildKafkaMessage(employeeAvro, TOPICS.get(2));
        employeeKafkaTemplate.send(payload);
        return employeeAvro;
    }
}
