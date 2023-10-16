package com.placide.k8skafkaavroaepccleanarchibsmicroscompany.infra.adapters.output.services;

import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.avrobeans.CompanyAvro;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.beans.address.Address;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.beans.company.Company;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.infra.adapters.output.mapper.CompanyMapper;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.infra.adapters.output.models.CompanyModel;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.infra.adapters.output.repository.CompanyRepository;
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
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Slf4j
class OutputKafkaProducerServiceImplTest {
    private final KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.
            parse("confluentinc/cp-kafka:latest"));
    @Mock
    private KafkaTemplate<String, CompanyAvro> companyKafkaTemplate;
    @Mock
    private CompanyRepository companyRepository;
    @InjectMocks
    private OutputKafkaProducerServiceImpl underTest;
    private static final String [] TOPICS ={"avro-companies-created", "avro-companies-deleted", "avro-companies-edited"};
    private Company company;
    private CompanyAvro companyAvro;
    private static final String ADDRESS_ID = "address_id";

    @BeforeEach
    void setUp() {
        kafkaContainer.start();
        MockitoAnnotations.openMocks(this);
        String bootstrapServers = kafkaContainer.getBootstrapServers();
        log.info("list of kafka container brokers: {}", bootstrapServers);
        System.setProperty("kafka.bootstrapAddress", bootstrapServers);

        Address address = new Address(ADDRESS_ID, 2, "Allée de la Haye du Temple", 59160,
                "Lomme", "France");

        company = new Company(
                UUID.randomUUID().toString(), "company-name", "Lomme", "esn",
                Timestamp.from(Instant.now()).toString(),ADDRESS_ID, address);
        companyAvro = CompanyMapper.fromBeanToAvro(company);
    }

    @Test
    void produceKafkaEventCompanyCreate() {
        //PREPARE
        Message<?> messageAvro = buildKafkaMessage(companyAvro,TOPICS[0]);
        //EXECUTE
        companyKafkaTemplate.send(messageAvro);
        CompanyAvro actual = underTest.produceKafkaEventCompanyCreate(companyAvro);
        //VERIFY
        Assertions.assertAll("gpe of assertions",
                () -> Mockito.verify(companyKafkaTemplate, Mockito.atLeast(1)).send(messageAvro),
                () -> Assertions.assertNotNull(actual));
    }

    @Test
    void produceKafkaEventCompanyDelete() {
        //PREPARE
        Message<?> message = buildKafkaMessage(companyAvro,TOPICS[1]);
        //EXECUTE
        CompanyAvro actual = underTest.produceKafkaEventCompanyDelete(companyAvro);
        companyKafkaTemplate.send(message);
        //VERIFY
        Assertions.assertAll("gpe of assertions", ()->{
            Mockito.verify(companyKafkaTemplate, Mockito.atLeast(1)).send(message);
            Assertions.assertNotNull(actual);
        });
    }

    @Test
    void produceKafkaEventCompanyEdit() {
        //PREPARE
        Message<?> message = buildKafkaMessage(companyAvro,TOPICS[2]);
        String id = "uuid-1";
        CompanyModel model = CompanyMapper.fromBeanToModel(company);
        //EXECUTE
        Mockito.when(companyRepository.findById(id)).thenReturn(Optional.of(model));
        CompanyAvro actual = underTest.produceKafkaEventCompanyEdit(companyAvro);
        companyKafkaTemplate.send(message);
        //VERIFY
        Assertions.assertAll("assertions",()->{
            Mockito.verify(companyKafkaTemplate, Mockito.atLeast(1)).send(message);
            Assertions.assertNotNull(actual);
        });
    }

    private Message<?> buildKafkaMessage(CompanyAvro avro, String topic){
       return MessageBuilder
                .withPayload(avro)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .build();
    }
}