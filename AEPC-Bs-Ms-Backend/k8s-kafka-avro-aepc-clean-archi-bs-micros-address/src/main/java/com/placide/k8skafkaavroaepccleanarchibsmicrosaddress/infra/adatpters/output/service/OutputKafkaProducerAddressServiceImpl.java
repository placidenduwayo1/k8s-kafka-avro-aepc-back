package com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.infra.adatpters.output.service;

import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.domain.ports.output.OutputKafkaProducerAddressService;
import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.domain.avrobean.AddressAvro;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutputKafkaProducerAddressServiceImpl implements OutputKafkaProducerAddressService {

    private final KafkaTemplate<String, AddressAvro> addressKafkaTemplate;
    private static final List<String> TOPICS = List.of("avro-addresses-created", "avro-addresses-deleted", "avro-addresses-edited");

    private Message<?> buildAddressAvro(AddressAvro addressAvro, String topic) {
        return MessageBuilder.withPayload(addressAvro)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .build();
    }

    @Override
    public AddressAvro sendKafkaAddressAddEvent(AddressAvro addressAvro) {

        Message<?> avroMessage = buildAddressAvro(addressAvro, TOPICS.get(0));
        addressKafkaTemplate.send(avroMessage);
        return addressAvro;
    }

    @Override
    public AddressAvro sendKafkaAddressDeleteEvent(AddressAvro addressAvro) {
        Message<?> avroMsg = buildAddressAvro(addressAvro, TOPICS.get(1));
        addressKafkaTemplate.send(avroMsg);
        return addressAvro;
    }

    @Override
    public AddressAvro sendKafkaAddressEditEvent(AddressAvro addressAvro) {

        Message<?> avroMsg = buildAddressAvro(addressAvro, TOPICS.get(2));
        addressKafkaTemplate.send(avroMsg);

        return addressAvro;
    }
}
