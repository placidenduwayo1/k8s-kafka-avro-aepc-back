package com.placide.k8skafkaavroaepccleanarchibsmicroscompany.infra.adapters.output.mapper;

import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.avrobean.CompanyAvro;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.bean.Company;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.infra.adapters.output.models.CompanyDto;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.infra.adapters.output.models.CompanyModel;
import org.springframework.beans.BeanUtils;

public class CompanyMapper {
    private CompanyMapper(){}
    public static CompanyModel fromBeanToModel(Company company){
        CompanyModel model = new CompanyModel();
        BeanUtils.copyProperties(company, model);
        return model;
    }
    public static Company fromModelToBean(CompanyModel model) {
        Company bean = new Company();
        BeanUtils.copyProperties(model,bean);
        return bean;
    }
    public static Company fromDtoToBean(CompanyDto dto){
        Company bean = new Company();
        BeanUtils.copyProperties(dto,bean);
        return bean;
    }

    public static CompanyDto fromBeanToDto(Company company) {
        CompanyDto dto = new CompanyDto();
        BeanUtils.copyProperties(company,dto);
        return dto;
    }

    public static CompanyAvro fromBeanToAvro(Company company){
        return CompanyAvro.newBuilder()
                .setCompanyId(company.getCompanyId())
                .setName(company.getName())
                .setAgency(company.getAgency())
                .setType(company.getType())
                .setConnectedDate(company.getConnectedDate())
                .build();
    }

    public static Company fromAvroToBean(CompanyAvro companyAvro){
        return new Company(
                companyAvro.getCompanyId(),
                companyAvro.getName(),
                companyAvro.getAgency(),
                companyAvro.getType(),
                companyAvro.getConnectedDate());
    }
}
