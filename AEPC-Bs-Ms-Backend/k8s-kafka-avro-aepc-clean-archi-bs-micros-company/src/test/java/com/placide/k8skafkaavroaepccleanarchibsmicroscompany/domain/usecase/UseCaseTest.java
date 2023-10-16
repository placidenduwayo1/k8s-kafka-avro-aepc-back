package com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.usecase;

import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.avrobeans.CompanyAvro;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.beans.address.Address;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.beans.company.Company;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.exceptions.*;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.ports.output.OutputCompanyService;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.ports.output.OutputKafkaProducerService;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.ports.output.OutputRemoteAddressService;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.infra.adapters.output.mapper.CompanyMapper;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.infra.adapters.output.models.CompanyDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

class UseCaseTest {
    @Mock
    private OutputKafkaProducerService kafkaProducerService;
    @Mock
    private OutputCompanyService companyService;
    @Mock
    private OutputRemoteAddressService remoteAddressService;
    @InjectMocks
    private UseCase underTest;
    private static final String COMPANY_ID = "company-id";
    private static final String NAME = "NATAN";
    private static final String AGENCY = "Paris";
    private static final String TYPE = "esn";
    private CompanyDto dto;
    private Address address;
    private static final String ADDRESS_ID = "address_id";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        address = new Address(ADDRESS_ID, 2, "Allée de la Haye du Temple", 59160,
                "Lomme", "France");
        dto = CompanyDto.builder()
                .name(NAME)
                .agency(AGENCY)
                .type(TYPE)
                .addressId(ADDRESS_ID)
                .build();
    }

    @Test
    void produceKafkaEventCompanyCreate() throws CompanyEmptyFieldsException, CompanyAlreadyExistsException, CompanyTypeInvalidException,
            RemoteApiAddressNotLoadedException {
        //PREPARE
        Company bean = CompanyMapper.fromDtoToBean(dto);
        bean.setCompanyId(UUID.randomUUID().toString());
        bean.setConnectedDate(Timestamp.from(Instant.now()).toString());
        bean.setAddressId(ADDRESS_ID);
        bean.setAddress(address);
        CompanyAvro avro = CompanyMapper.fromBeanToAvro(bean);
        //EXECUTE
        Mockito.when(remoteAddressService.getRemoteAddressById(ADDRESS_ID)).thenReturn(Optional.of(address));
        Mockito.when(kafkaProducerService.produceKafkaEventCompanyCreate(Mockito.any(CompanyAvro.class))).thenReturn(avro);
        Company actual = underTest.produceKafkaEventCompanyCreate(dto);
        //VERIFY
        Assertions.assertAll("assertions", () -> {
            Mockito.verify(remoteAddressService, Mockito.atLeast(1)).getRemoteAddressById(ADDRESS_ID);
            Mockito.verify(kafkaProducerService, Mockito.atLeast(1)).produceKafkaEventCompanyCreate(Mockito.any(CompanyAvro.class));
            Assertions.assertEquals(bean.getCompanyId(), actual.getCompanyId());
            Assertions.assertEquals(bean.getName(), actual.getName());
            Assertions.assertEquals(bean.getAgency(), actual.getAgency());
            Assertions.assertEquals(bean.getConnectedDate(), actual.getConnectedDate());
        });
    }

    @Test
    void createCompany() throws RemoteApiAddressNotLoadedException {
        //PREPARE
        Company bean = CompanyMapper.fromDtoToBean(dto);
        //EXECUTE
        Mockito.when(remoteAddressService.getRemoteAddressById(ADDRESS_ID)).thenReturn(Optional.of(address));
        Mockito.when(companyService.saveCompany(Mockito.any(Company.class))).thenReturn(bean);
        Company actual = underTest.createCompany(bean);
        //VERIFY
        Assertions.assertAll("assertions", () -> {
            Mockito.verify(remoteAddressService, Mockito.atLeast(1)).getRemoteAddressById(ADDRESS_ID);
            Mockito.verify(companyService, Mockito.atLeast(1)).saveCompany(bean);
            Assertions.assertEquals(bean, actual);
        });
    }

    @Test
    void getCompanyById() throws CompanyNotFoundException, RemoteApiAddressNotLoadedException {
        //PREPARE
        Company bean = CompanyMapper.fromDtoToBean(dto);
        //EXECUTE
        Mockito.when(remoteAddressService.getRemoteAddressById(ADDRESS_ID)).thenReturn(Optional.of(address));
        Mockito.when(companyService.getCompanyById(COMPANY_ID)).thenReturn(Optional.of(bean));
        Company actual = underTest.getCompanyById(COMPANY_ID).orElseThrow(CompanyNotFoundException::new);
        //VERIFY
        Assertions.assertAll("assertions", () -> {
            Mockito.verify(remoteAddressService, Mockito.atLeast(1)).getRemoteAddressById(ADDRESS_ID);
            Mockito.verify(companyService, Mockito.atLeast(1)).getCompanyById(COMPANY_ID);
            Assertions.assertEquals(bean, actual);
        });
    }

    @Test
    void loadCompanyByInfo() {
        //PREPARE
        List<Company> beans = List.of(CompanyMapper.fromDtoToBean(dto));
        //EXECUTE
        Mockito.when(companyService.loadCompanyByInfo(NAME, AGENCY, TYPE)).thenReturn(beans);
        List<Company> actuals = underTest.loadCompanyByInfo(NAME, AGENCY, TYPE);
        //VERIFY
        Assertions.assertAll("assertions", () -> {
            Mockito.verify(companyService, Mockito.atLeast(1)).loadCompanyByInfo(NAME, AGENCY, TYPE);
            Assertions.assertEquals(1, actuals.size());
        });
    }

    @Test
    void loadAllCompanies() {
        //PREPARE
        List<Company> beans = List.of(CompanyMapper.fromDtoToBean(dto));
        //EXECUTE
        Mockito.when(companyService.loadAllCompanies()).thenReturn(beans);
        List<Company> actuals = underTest.loadAllCompanies();
        //VERIFY
        Assertions.assertAll("assertions", () -> {
            Mockito.verify(companyService, Mockito.atLeast(1)).loadAllCompanies();
            Assertions.assertEquals(1, actuals.size());
        });
    }

    @Test
    void produceKafkaEventCompanyDelete() throws CompanyNotFoundException, RemoteApiAddressNotLoadedException {
        //PREPARE
        Company bean = CompanyMapper.fromDtoToBean(dto);
        bean.setCompanyId(UUID.randomUUID().toString());
        bean.setConnectedDate(Timestamp.from(Instant.now()).toString());
        bean.setAddressId(ADDRESS_ID);
        bean.setAddress(address);
        CompanyAvro avro = CompanyMapper.fromBeanToAvro(bean);
        //EXECUTE
        Mockito.when(remoteAddressService.getRemoteAddressById(ADDRESS_ID)).thenReturn(Optional.of(address));
        Mockito.when(companyService.getCompanyById(COMPANY_ID)).thenReturn(Optional.of(bean));
        Company actual1 = underTest.getCompanyById(COMPANY_ID).orElseThrow(CompanyNotFoundException::new);
        Mockito.when(kafkaProducerService.produceKafkaEventCompanyDelete(avro)).thenReturn(avro);
        Company actual2 = underTest.produceKafkaEventCompanyDelete(COMPANY_ID);
        //VERIFY
        Assertions.assertAll("gpe of assertions", () -> {
            Mockito.verify(remoteAddressService, Mockito.atLeast(1)).getRemoteAddressById(ADDRESS_ID);
            Mockito.verify(companyService, Mockito.atLeast(1)).getCompanyById(COMPANY_ID);
            Mockito.verify(kafkaProducerService, Mockito.atLeast(1)).produceKafkaEventCompanyDelete(avro);
            Assertions.assertEquals(actual1.toString(), actual2.toString());
        });
    }

    @Test
    void deleteCompany() throws CompanyNotFoundException, RemoteApiAddressNotLoadedException {
        //PREPARE
        Company bean = CompanyMapper.fromDtoToBean(dto);
        //EXECUTE
        Mockito.when(remoteAddressService.getRemoteAddressById(ADDRESS_ID)).thenReturn(Optional.of(address));
        Mockito.when(companyService.getCompanyById(COMPANY_ID)).thenReturn(Optional.of(bean));
        Company actual = underTest.getCompanyById(COMPANY_ID).orElseThrow(CompanyNotFoundException::new);
        Mockito.when(companyService.deleteCompany(actual.getCompanyId())).thenReturn("");
        String msg = underTest.deleteCompany(COMPANY_ID);
        //VERIFY
        Assertions.assertAll("gpe of assertions", () -> {
            Mockito.verify(remoteAddressService, Mockito.atLeast(1)).getRemoteAddressById(ADDRESS_ID);
            Mockito.verify(companyService, Mockito.atLeast(1)).getCompanyById(COMPANY_ID);
            Mockito.verify(companyService, Mockito.atLeast(1)).deleteCompany(actual.getCompanyId());
            Assertions.assertEquals("Company <" + actual + "> successfully deleted", msg);
        });
    }

    @Test
    void produceKafkaEventCompanyEdit() throws CompanyNotFoundException, CompanyEmptyFieldsException, CompanyTypeInvalidException,
            RemoteApiAddressNotLoadedException {
        //PREPARE
        String companyId = "company-id";
        Company bean = CompanyMapper.fromDtoToBean(dto);
        bean.setCompanyId(companyId);
        bean.setConnectedDate(Timestamp.from(Instant.now()).toString());
        bean.setAddressId(ADDRESS_ID);
        bean.setAddress(address);
        CompanyAvro avro = CompanyMapper.fromBeanToAvro(bean);
        //EXECUTE
        Mockito.when(remoteAddressService.getRemoteAddressById(ADDRESS_ID)).thenReturn(Optional.of(address));
        Mockito.when(companyService.getCompanyById(companyId)).thenReturn(Optional.of(bean));
        Mockito.when(kafkaProducerService.produceKafkaEventCompanyEdit(Mockito.any(CompanyAvro.class)))
                .thenReturn(avro);
        Company actual = underTest.produceKafkaEventCompanyEdit(dto, COMPANY_ID);
        //VERIFY
        Assertions.assertAll("gpe of assertions", () -> {
            Mockito.verify(remoteAddressService, Mockito.atLeast(1)).getRemoteAddressById(ADDRESS_ID);
            Mockito.verify(kafkaProducerService, Mockito.atLeast(
                            1))
                    .produceKafkaEventCompanyEdit(Mockito.any(CompanyAvro.class));
            Assertions.assertEquals(bean.toString(), actual.toString());
        });
    }

    @Test
    void editCompany() throws CompanyNotFoundException, RemoteApiAddressNotLoadedException {
        //PREPARE
        Company bean = CompanyMapper.fromDtoToBean(dto);
        bean.setCompanyId(UUID.randomUUID().toString());
        bean.setConnectedDate(Timestamp.from(Instant.now()).toString());
        //EXECUTE
        Mockito.when(remoteAddressService.getRemoteAddressById(ADDRESS_ID)).thenReturn(Optional.of(address));
        Mockito.when(companyService.getCompanyById(COMPANY_ID)).thenReturn(Optional.of(bean));
        Company actual1 = underTest.getCompanyById(COMPANY_ID).orElseThrow(CompanyNotFoundException::new);
        Mockito.when(companyService.editCompany(bean)).thenReturn(bean);
        Company actual2 = underTest.editCompany(bean);
        //VERIFY
        Assertions.assertAll("assertions", () -> {
            Mockito.verify(remoteAddressService, Mockito.atLeast(1)).getRemoteAddressById(ADDRESS_ID);
            Mockito.verify(companyService, Mockito.atLeast(1)).getCompanyById(COMPANY_ID);
            Mockito.verify(companyService, Mockito.atLeast(1)).editCompany(bean);
            Assertions.assertEquals(bean, actual2);
            Assertions.assertEquals(actual1, actual2);
        });
    }
}