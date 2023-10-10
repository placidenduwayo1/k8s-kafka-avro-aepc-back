package com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.usecase;

import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.avrobean.CompanyAvro;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.ports.input.InputCompanyService;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.ports.output.OutputCompanyService;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.ports.output.OutputKafkaProducerService;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.bean.Company;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.exceptions.CompanyAlreadyExistsException;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.exceptions.CompanyEmptyFieldsException;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.exceptions.CompanyNotFoundException;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.exceptions.CompanyTypeInvalidException;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.infra.adapters.output.mapper.CompanyMapper;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.infra.adapters.output.models.CompanyDto;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UseCase implements InputCompanyService {
    private final OutputKafkaProducerService kafkaProducerService;
    private final OutputCompanyService companyService;

    public UseCase(OutputKafkaProducerService kafkaProducerService, OutputCompanyService companyService) {
        this.kafkaProducerService = kafkaProducerService;
        this.companyService = companyService;
    }

    private void checkPayloadValidity(CompanyDto dto) throws CompanyEmptyFieldsException, CompanyTypeInvalidException{
        if(!Validator.areValidCompanyFields(dto.getName(), dto.getAgency(), dto.getType())) {
            throw new CompanyEmptyFieldsException();
        }
        else if(!Validator.checkTypeExists(dto.getType())){
            throw new CompanyTypeInvalidException();
        }
    }

    private void checkCompanyAlreadyExists(CompanyDto dto) throws CompanyAlreadyExistsException {
        if(!loadCompanyByInfo(dto.getName(), dto.getAgency(), dto.getType()).isEmpty()){
            throw new CompanyAlreadyExistsException();
        }
    }
    @Override
    public Company produceKafkaEventCompanyCreate(CompanyDto dto) throws CompanyTypeInvalidException, CompanyEmptyFieldsException,
            CompanyAlreadyExistsException {
        Validator.format(dto);
        checkPayloadValidity(dto);
        checkCompanyAlreadyExists(dto);
        Company bean = CompanyMapper.fromDtoToBean(dto);
        bean.setCompanyId(UUID.randomUUID().toString());
        bean.setConnectedDate(Timestamp.from(Instant.now()).toString());
        CompanyAvro companyAvro = CompanyMapper.fromBeanToAvro(bean);
        return CompanyMapper.fromAvroToBean(kafkaProducerService.produceKafkaEventCompanyCreate(companyAvro));
    }

    @Override
    public Company createCompany(Company company) {
        return companyService.saveCompany(company);
    }

    @Override
    public Optional<Company> getCompanyById(String id) throws CompanyNotFoundException {
        return Optional.of(companyService.getCompanyById(id)).orElseThrow(CompanyNotFoundException::new);
    }

    @Override
    public List<Company> loadCompanyByInfo(String name, String agency, String type) {
        return companyService.loadCompanyByInfo(name,agency,type);
    }

    @Override
    public List<Company> loadAllCompanies() {
        return companyService.loadAllCompanies();
    }

    @Override
    public Company produceKafkaEventCompanyDelete(String id) throws CompanyNotFoundException {
        Company company = getCompanyById(id).orElseThrow(CompanyNotFoundException::new);
        CompanyAvro companyAvro = CompanyMapper.fromBeanToAvro(company);
        return CompanyMapper.fromAvroToBean(kafkaProducerService.produceKafkaEventCompanyDelete(companyAvro));
    }

    @Override
    public String deleteCompany(String id) throws CompanyNotFoundException {
       Company company = getCompanyById(id).orElseThrow(CompanyNotFoundException::new);
       companyService.deleteCompany(company.getCompanyId());
        return "Company <"+company+"> successfully deleted";
    }

    @Override
    public Company produceKafkaEventCompanyEdit(CompanyDto payload, String id) throws CompanyNotFoundException,
            CompanyTypeInvalidException, CompanyEmptyFieldsException {
        Validator.format(payload);
        checkPayloadValidity(payload);
        Company company = getCompanyById(id).orElseThrow(CompanyNotFoundException::new);
        company.setName(payload.getName());
        company.setAgency(payload.getAgency());
        company.setType(payload.getType());
        CompanyAvro companyAvro = CompanyMapper.fromBeanToAvro(company);
        return CompanyMapper.fromAvroToBean(kafkaProducerService.produceKafkaEventCompanyEdit(companyAvro));
    }

    @Override
    public Company editCompany(Company payload) {
        return companyService.editCompany(payload);
    }
}
