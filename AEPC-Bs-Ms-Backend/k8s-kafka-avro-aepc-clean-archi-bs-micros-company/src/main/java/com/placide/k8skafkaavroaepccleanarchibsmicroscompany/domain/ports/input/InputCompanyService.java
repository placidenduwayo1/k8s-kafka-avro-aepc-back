package com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.ports.input;

import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.bean.Company;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.exceptions.CompanyAlreadyExistsException;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.exceptions.CompanyEmptyFieldsException;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.exceptions.CompanyNotFoundException;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.exceptions.CompanyTypeInvalidException;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.infra.adapters.output.models.CompanyDto;

import java.util.List;
import java.util.Optional;

public interface InputCompanyService {
    Company produceKafkaEventCompanyCreate(CompanyDto dto) throws
            CompanyTypeInvalidException, CompanyEmptyFieldsException, CompanyAlreadyExistsException;
    Company createCompany(Company company) throws
            CompanyAlreadyExistsException, CompanyEmptyFieldsException,
            CompanyTypeInvalidException;
    Optional<Company> getCompanyById(String id) throws CompanyNotFoundException;
    List<Company> loadCompanyByInfo(String name, String agency, String type);
    List<Company> loadAllCompanies();
    Company produceKafkaEventCompanyDelete(String id) throws CompanyNotFoundException;
    String deleteCompany(String id) throws CompanyNotFoundException;
    Company produceKafkaEventCompanyEdit(CompanyDto payload, String id) throws
            CompanyNotFoundException, CompanyTypeInvalidException, CompanyEmptyFieldsException;
    Company editCompany(Company payload);
}
