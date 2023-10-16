package com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.usecase;

import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.avrobeans.CompanyAvro;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.beans.address.Address;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.exceptions.*;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.ports.input.InputCompanyService;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.ports.input.InputRemoteAddressService;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.ports.output.OutputCompanyService;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.ports.output.OutputKafkaProducerService;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.beans.company.Company;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.ports.output.OutputRemoteAddressService;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.infra.adapters.output.mapper.CompanyMapper;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.infra.adapters.output.models.CompanyDto;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UseCase implements InputCompanyService, InputRemoteAddressService {
    private final OutputKafkaProducerService kafkaProducerService;
    private final OutputCompanyService companyService;
    private final OutputRemoteAddressService outputRemoteAddressService;

    public UseCase(OutputKafkaProducerService kafkaProducerService, OutputCompanyService companyService, OutputRemoteAddressService outputRemoteAddressService) {
        this.kafkaProducerService = kafkaProducerService;
        this.companyService = companyService;
        this.outputRemoteAddressService = outputRemoteAddressService;
    }

    private void checkPayloadValidity(CompanyDto dto) throws CompanyEmptyFieldsException, CompanyTypeInvalidException, RemoteApiAddressNotLoadedException {
        if(!Validator.areValidCompanyFields(dto.getName(), dto.getAgency(), dto.getType(), dto.getAddressId())) {
            throw new CompanyEmptyFieldsException();
        }
        else if(!Validator.checkTypeExists(dto.getType())){
            throw new CompanyTypeInvalidException();
        }
        Address address = getRemoteAddressById(dto.getAddressId()).orElseThrow(RemoteApiAddressNotLoadedException::new);
        if (Validator.remoteAddressApiUnreachable(address.getAddressId())) {
            throw new RemoteApiAddressNotLoadedException();
        }
    }

    private void checkCompanyAlreadyExists(CompanyDto dto) throws CompanyAlreadyExistsException {
        if(!loadCompanyByInfo(dto.getName(), dto.getAgency(), dto.getType()).isEmpty()){
            throw new CompanyAlreadyExistsException();
        }
    }

    private void setCompanyDependency(Company company, String addressId) throws RemoteApiAddressNotLoadedException {
        Address address = getRemoteAddressById(addressId).orElseThrow(RemoteApiAddressNotLoadedException::new);
        company.setAddressId(addressId);
        company.setAddress(address);
    }
    @Override
    public Company produceKafkaEventCompanyCreate(CompanyDto dto) throws CompanyTypeInvalidException, CompanyEmptyFieldsException,
            CompanyAlreadyExistsException, RemoteApiAddressNotLoadedException {
        Validator.format(dto);
        checkPayloadValidity(dto);
        checkCompanyAlreadyExists(dto);
        Company bean = CompanyMapper.fromDtoToBean(dto);
        bean.setCompanyId(UUID.randomUUID().toString());
        bean.setConnectedDate(Timestamp.from(Instant.now()).toString());
        setCompanyDependency(bean, dto.getAddressId());
        CompanyAvro companyAvro = CompanyMapper.fromBeanToAvro(bean);
        return CompanyMapper.fromAvroToBean(kafkaProducerService.produceKafkaEventCompanyCreate(companyAvro));
    }

    @Override
    public Company createCompany(Company company) throws RemoteApiAddressNotLoadedException {
        Company saved = companyService.saveCompany(company);
        setCompanyDependency(saved, saved.getAddressId());
        return saved;
    }

    @Override
    public Optional<Company> getCompanyById(String id) throws CompanyNotFoundException, RemoteApiAddressNotLoadedException {
       Company company = companyService.getCompanyById(id).orElseThrow(CompanyNotFoundException::new);
       setCompanyDependency(company, company.getAddressId());
       return Optional.of(company);
    }

    @Override
    public List<Company> loadCompanyByInfo(String name, String agency, String type) {
        List<Company>  companies = companyService.loadCompanyByInfo(name,agency,type);
        companies.forEach(company -> {
            try {
                setCompanyDependency(company,company.getAddressId());
            } catch (RemoteApiAddressNotLoadedException e) {
                e.getMessage();
            }
        });

        return companies;
    }

    @Override
    public List<Company> loadAllCompanies() {
        List<Company> companies = companyService.loadAllCompanies();
        companies.forEach(company -> {
            try {
                setCompanyDependency(company,company.getAddressId());
            } catch (RemoteApiAddressNotLoadedException e) {
                e.getMessage();
            }
        });

        return companies;
    }

    @Override
    public Company produceKafkaEventCompanyDelete(String id) throws CompanyNotFoundException, RemoteApiAddressNotLoadedException {
        Company company = getCompanyById(id).orElseThrow(CompanyNotFoundException::new);
        setCompanyDependency(company, company.getAddressId());
        CompanyAvro companyAvro = CompanyMapper.fromBeanToAvro(company);
        return CompanyMapper.fromAvroToBean(kafkaProducerService.produceKafkaEventCompanyDelete(companyAvro));
    }

    @Override
    public String deleteCompany(String id) throws CompanyNotFoundException, RemoteApiAddressNotLoadedException {
       Company company = getCompanyById(id).orElseThrow(CompanyNotFoundException::new);
       companyService.deleteCompany(company.getCompanyId());
        return "Company <"+company+"> successfully deleted";
    }

    @Override
    public Company produceKafkaEventCompanyEdit(CompanyDto payload, String id) throws CompanyNotFoundException,
            CompanyTypeInvalidException, CompanyEmptyFieldsException, RemoteApiAddressNotLoadedException {
        Validator.format(payload);
        checkPayloadValidity(payload);
        Company company = getCompanyById(id).orElseThrow(CompanyNotFoundException::new);
        company.setName(payload.getName());
        company.setAgency(payload.getAgency());
        company.setType(payload.getType());
        setCompanyDependency(company, company.getAddressId());
        CompanyAvro companyAvro = CompanyMapper.fromBeanToAvro(company);
        return CompanyMapper.fromAvroToBean(kafkaProducerService.produceKafkaEventCompanyEdit(companyAvro));
    }

    @Override
    public Company editCompany(Company payload) throws RemoteApiAddressNotLoadedException {
        Company company = companyService.editCompany(payload);
        setCompanyDependency(company, company.getAddressId());
        return company;
    }

    @Override
    public Optional<Address> getRemoteAddressById(String addressId) throws RemoteApiAddressNotLoadedException {
        return Optional.ofNullable(outputRemoteAddressService.getRemoteAddressById(addressId)
                .orElseThrow(RemoteApiAddressNotLoadedException::new));
    }
}
