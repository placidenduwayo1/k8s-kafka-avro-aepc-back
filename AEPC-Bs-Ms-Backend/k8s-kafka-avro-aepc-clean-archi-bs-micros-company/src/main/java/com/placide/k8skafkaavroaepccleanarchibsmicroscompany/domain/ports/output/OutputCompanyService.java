package com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.ports.output;

import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.avrobeans.CompanyAvro;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.beans.company.Company;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.exceptions.CompanyNotFoundException;

import java.util.List;
import java.util.Optional;

public interface OutputCompanyService {
    Company consumeKafkaEventCompanyCreate(CompanyAvro companyAvro, String topic);
    Company saveCompany(Company company);
    Optional<Company> getCompanyById(String id) throws CompanyNotFoundException;
    List<Company> loadCompanyByInfo(String name, String agency,String type);
    List<Company> loadAllCompanies();
    Company consumeKafkaEventCompanyDelete(CompanyAvro companyAvro, String topic);
    String deleteCompany(String id) throws CompanyNotFoundException;
    Company consumeKafkaEventCompanyEdit(CompanyAvro companyAvro, String topic);
    Company editCompany(Company company);
}
