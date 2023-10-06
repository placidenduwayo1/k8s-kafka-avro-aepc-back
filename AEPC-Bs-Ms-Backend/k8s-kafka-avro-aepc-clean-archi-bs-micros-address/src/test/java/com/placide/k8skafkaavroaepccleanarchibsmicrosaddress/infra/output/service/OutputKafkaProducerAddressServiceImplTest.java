package com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.infra.output.service;

import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.domain.bean.Address;
import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.infra.adatpters.output.mapper.AddressMapper;
import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.infra.adatpters.output.repository.AddressRepository;
import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.infra.adatpters.output.service.OutputKafkaProducerAddressServiceImpl;
import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.domain.avrobean.AddressAvro;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
class OutputKafkaProducerAddressServiceImplTest {
    private final KafkaContainer kafkaContainer = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:latest"));
    @Mock
    private KafkaTemplate<String, AddressAvro> kafkaTemplate;
    @Mock
    private AddressRepository addressRepositoryMock;
    @InjectMocks
    private OutputKafkaProducerAddressServiceImpl underTest;
    private static final List<String> TOPICS = List.of("avro-addresses-created", "avro-addresses-deleted", "avro-addresses-edited");

    @BeforeEach
    void setUp() {
        kafkaContainer.start();
        String bootstrapServers = kafkaContainer.getBootstrapServers();
        log.info("list of kafka container brokers: {}", bootstrapServers);
        System.setProperty("kafka.bootstrapAddress", bootstrapServers);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendKafkaAddressAddEvent() {
        //PREPARE
        Address address = new Address(
                UUID.randomUUID().toString(),
                184, "Avenue de Liège",
                59300, "Valenciennes",
                "France");
        AddressAvro addressAvro = AddressAvro.newBuilder()
                .setAddressId(address.getAddressId())
                .setNum(address.getNum())
                .setStreet(address.getStreet())
                .setPoBox(address.getPoBox())
                .setCity(address.getCity())
                .setCountry(address.getCountry())
                .build();
        Message<?> avroMessage = buildKafkaMessage(addressAvro,TOPICS.get(0));
        //EXECUTE
        kafkaTemplate.send(avroMessage);
        AddressAvro sentAddress = underTest.sendKafkaAddressAddEvent(addressAvro);
        //VERIFY
        Assertions.assertAll("gpe of assertions",
                () -> Mockito.verify(kafkaTemplate, Mockito.atLeast(1)).send(avroMessage),
                () -> Assertions.assertNotNull(sentAddress));
    }

    @Test
    void sendKafkaAddressDeleteEvent() {
        //PREPARE
        Address address = new Address(
                UUID.randomUUID().toString(),
                184, "Avenue de Liège",
                59300, "Valenciennes",
                "France");
        AddressAvro addressAvro = AddressMapper.mapBeanToAvro(address);
        Message<?> message = buildKafkaMessage(addressAvro,TOPICS.get(1));
        //EXECUTE
        AddressAvro actual = underTest.sendKafkaAddressDeleteEvent(addressAvro);
        kafkaTemplate.send(message);
        //VERIFY
        Assertions.assertAll("gpe of assertions",
                () -> Mockito.verify(kafkaTemplate, Mockito.atLeast(1)).send(message),
                () -> Assertions.assertNotNull(actual));
    }

    @Test
    void sendKafkaAddressEditEvent() {
        //PREPARE
        Address address = new Address(
                UUID.randomUUID().toString(),
                44, "Rue Notre Dame des Victoires",
                74002, "Paris",
                "France");
        AddressAvro addressAvro = AddressMapper.mapBeanToAvro(address);
        Message<?> message = buildKafkaMessage(addressAvro,TOPICS.get(1));
        //EXECUTE
        AddressAvro sentAddress = underTest.sendKafkaAddressEditEvent(addressAvro);
        kafkaTemplate.send(message);
        //VERIFY
        Assertions.assertAll("gpe of assertions",
                () -> Mockito.verify(kafkaTemplate, Mockito.atLeast(1)).send(message),
                () -> Assertions.assertNotNull(sentAddress));
    }

    private Message<?> buildKafkaMessage(AddressAvro avro, String topic){
        return MessageBuilder.withPayload(avro)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .build();
    }
}