package com.placide.k8skafkaavroaepccleanarchibsmicroscompany.infra.adapters.output.services;

import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.avrobean.CompanyAvro;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.ports.output.OutputCompanyService;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.bean.Company;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.exceptions.CompanyNotFoundException;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.infra.adapters.output.mapper.CompanyMapper;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.infra.adapters.output.models.CompanyModel;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.infra.adapters.output.repository.CompanyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@Slf4j
public class OutputCompanyServiceImpl implements OutputCompanyService {
    private final CompanyRepository repository;

    public OutputCompanyServiceImpl(CompanyRepository repository) {
        this.repository = repository;
    }

    @Override
    @KafkaListener(topics = "avro-companies-created",groupId = "company-group-id")
    public Company consumeKafkaEventCompanyCreate(@Payload CompanyAvro companyAvro, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        Company company = CompanyMapper.fromAvroToBean(companyAvro);
        log.info("company to create:<{}> consumed from topic:<{}>",company,topic);
        return company;
    }

    @Override
    public Company saveCompany(Company company) {
        CompanyAvro companyAvro = CompanyMapper.fromBeanToAvro(company);
        Company consumed = consumeKafkaEventCompanyCreate(companyAvro,"company-group-id");
        CompanyModel savedModel = repository.save(CompanyMapper.fromBeanToModel(consumed));
        return CompanyMapper.fromModelToBean(savedModel);
    }

    @Override
    public Optional<Company> getCompanyById(String id) throws CompanyNotFoundException {
        CompanyModel model =repository.findById(id).orElseThrow(CompanyNotFoundException::new);
        return Optional.of(CompanyMapper.fromModelToBean(model));
    }

    private List<Company> mapToBean(List<CompanyModel> models){
       return models.stream()
                .map(CompanyMapper::fromModelToBean)
                .toList();
    }

    @Override
    public List<Company> loadCompanyByInfo(String name, String agency, String type) {
        List<CompanyModel> models = repository.findByNameAndAgencyAndType(name,agency,type);
        return mapToBean(models);
    }

    @Override
    public List<Company> loadAllCompanies() {
        List<CompanyModel> models = repository.findAll();
        return mapToBean(models);
    }

    @Override
    @KafkaListener(topics = "avro-companies-deleted",groupId = "company-group-id")
    public Company consumeKafkaEventCompanyDelete(@Payload CompanyAvro companyAvro, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        Company company = CompanyMapper.fromAvroToBean(companyAvro);
        log.info("company to delete:<{}> consumed from topic:<{}>",company,topic);
        return company;
    }

    @Override
    public String deleteCompany(String id) throws CompanyNotFoundException {
        Company company = getCompanyById(id).orElseThrow(CompanyNotFoundException::new);
        CompanyAvro companyAvro = CompanyMapper.fromBeanToAvro(company);
        Company consumed = consumeKafkaEventCompanyDelete(companyAvro,"${topics.names.topic2}");
        repository.deleteById(consumed.getCompanyId());
        return "company <"+consumed+"> deleted";
    }

    @Override
    @KafkaListener(topics = "avro-companies-edited",groupId = "company-group-id")
    public Company consumeKafkaEventCompanyEdit(@Payload CompanyAvro companyAvro, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        Company company = CompanyMapper.fromAvroToBean(companyAvro);
        log.info("company to edit:<{}> consumed from topic:<{}>",company,topic);
        return company;
    }

    @Override
    public Company editCompany(Company company) {
        CompanyAvro companyAvro = CompanyMapper.fromBeanToAvro(company);
        Company consumed = consumeKafkaEventCompanyEdit(companyAvro,"${topics.names.topic3}");
        CompanyModel mapped = CompanyMapper.fromBeanToModel(consumed);
        return CompanyMapper.fromModelToBean(repository.save(mapped));
    }
}
