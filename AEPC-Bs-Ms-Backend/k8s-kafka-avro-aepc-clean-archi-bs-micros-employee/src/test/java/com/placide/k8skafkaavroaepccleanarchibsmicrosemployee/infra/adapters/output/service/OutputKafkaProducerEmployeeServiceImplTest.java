package com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.infra.adapters.output.service;

import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.avrobeans.EmployeeAvro;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.beans.address.Address;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.beans.employee.Employee;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.exceptions.RemoteApiAddressNotLoadedException;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.infra.adapters.input.feignclients.models.AddressModel;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.infra.adapters.input.feignclients.proxy.AddressServiceProxy;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.infra.adapters.output.mapper.AddressMapper;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.infra.adapters.output.mapper.EmployeeMapper;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.infra.adapters.output.models.EmployeeModel;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.infra.adapters.output.repository.EmployeeRepository;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
class OutputKafkaProducerEmployeeServiceImplTest {
    private final KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.
            parse("confluentinc/cp-kafka:latest"));
    @Mock
    private KafkaTemplate<String, EmployeeAvro> employeeAvroKafkaTemplate;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private AddressServiceProxy addressServiceProxy;
    @InjectMocks
    private OutputKafkaProducerEmployeeServiceImpl underTest;
    private static final List<String> TOPICS = List.of("avro-employees-created", "avro-employees-deleted", "avro-employees-edited");
    private Employee employee;
    private Address address;
    private EmployeeAvro employeeAvro;
    private static final String ADDRESS_ID = "address_id";

    @BeforeEach
    void setUp() {
        kafkaContainer.start();
        MockitoAnnotations.openMocks(this);
        String bootstrapServers = kafkaContainer.getBootstrapServers();
        log.info("list of kafka container brokers: {}", bootstrapServers);
        System.setProperty("kafka.bootstrapAddress", bootstrapServers);

        address = new Address(ADDRESS_ID, 184, "Avenue de Liège", 59300,
                "Valenciennes", "France");

        employee = new Employee(
                UUID.randomUUID().toString(), "Placide", "Nduwayo", "placide.nduwayo@domain.com",
                Timestamp.from(Instant.now()).toString(), "active", "software-engineer", "null-address-id", address);
        employeeAvro = EmployeeMapper.fromBeanToAvro(employee);
    }

    @Test
    void produceKafkaEventEmployeeCreate() {
        //PREPARE
        Message<?> message = buildKafkaMessage(employeeAvro, TOPICS.get(0));
        //EXECUTE
        employeeAvroKafkaTemplate.send(message);
        EmployeeAvro actual = underTest.produceKafkaEventEmployeeCreate(employeeAvro);
        //VERIFY
        Assertions.assertAll("gpe of assertions",()->{
            Mockito.verify(employeeAvroKafkaTemplate).send(message);
            Assertions.assertNotNull(actual);
        });
    }

    @Test
    void produceKafkaEventEmployeeDelete() throws RemoteApiAddressNotLoadedException {
        //PREPARE
        Message<?> message = buildKafkaMessage(employeeAvro, TOPICS.get(1));
        AddressModel addressModel = AddressMapper.toModel(address);
        //EXECUTE
        Mockito.when(addressServiceProxy.loadRemoteApiGetAddressById(employee.getAddressId())).thenReturn(addressModel);
        EmployeeAvro actual = underTest.produceKafkaEventEmployeeDelete(employeeAvro);
        employeeAvroKafkaTemplate.send(message);
        //VERIFY
        Assertions.assertAll("assertions", () -> {
            Mockito.verify(employeeAvroKafkaTemplate, Mockito.atLeast(1)).send(message);
            Assertions.assertNotNull(actual);
        });
    }

    @Test
    void produceKafkaEventEmployeeEdit() throws RemoteApiAddressNotLoadedException {
        //PREPARE
        String employeeId = "uuid-1";
        String addressId = "uuid-2";
        Employee bean = new Employee(
                employeeId, "Placide", "Nduwayo", "placide.nduwayo@domain.com",
                Timestamp.from(Instant.now()).toString(), "active", "software-engineer", "null-address-id", address);
        EmployeeAvro avro = EmployeeMapper.fromBeanToAvro(bean);
        Message<?> message = buildKafkaMessage(avro, TOPICS.get(2));
        AddressModel addressModel = new AddressModel(addressId, 184, "Avenue de Liège", 59300,
                "Valenciennes", "France");
        EmployeeModel employeeModel = EmployeeMapper.toModel(bean);
        //EXECUTE
        Mockito.when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employeeModel));
        Mockito.when(addressServiceProxy.loadRemoteApiGetAddressById(employee.getAddressId())).thenReturn(addressModel);
        EmployeeAvro actual = underTest.produceKafkaEventEmployeeEdit(avro);
        employeeAvroKafkaTemplate.send(message);
        //VERIFY
        Assertions.assertAll("gpe of assertions",()->{
            Mockito.verify(employeeAvroKafkaTemplate).send(message);
            Assertions.assertNotNull(actual);
            Assertions.assertEquals(actual.getEmployeeId(),avro.getEmployeeId());
            Assertions.assertEquals(actual.getFirstname(),avro.getFirstname());
            Assertions.assertEquals(actual.getLastname(),avro.getLastname());
            Assertions.assertEquals(actual.getEmail(),avro.getEmail());
            Assertions.assertEquals(actual.getState(),avro.getState());
            Assertions.assertEquals(actual.getType(),avro.getType());
            Assertions.assertEquals(actual.getHireDate(),avro.getHireDate());
            Assertions.assertEquals(actual.getAddress(),avro.getAddress());
        });
    }
    private Message<?> buildKafkaMessage(EmployeeAvro payload, String topic) {
        return MessageBuilder.withPayload(payload)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .build();
    }
}