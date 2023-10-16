package com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.ports.input;

import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.beans.company.Company;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.exceptions.*;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.infra.adapters.output.models.CompanyDto;

import java.util.List;
import java.util.Optional;

public interface InputCompanyService {
    Company produceKafkaEventCompanyCreate(CompanyDto dto) throws
            CompanyTypeInvalidException, CompanyEmptyFieldsException, CompanyAlreadyExistsException, RemoteApiAddressNotLoadedException;
    Company createCompany(Company company) throws
            CompanyAlreadyExistsException, CompanyEmptyFieldsException,
            CompanyTypeInvalidException, RemoteApiAddressNotLoadedException;
    Optional<Company> getCompanyById(String id) throws CompanyNotFoundException, RemoteApiAddressNotLoadedException;
    List<Company> loadCompanyByInfo(String name, String agency, String type);
    List<Company> loadAllCompanies();
    Company produceKafkaEventCompanyDelete(String id) throws CompanyNotFoundException, RemoteApiAddressNotLoadedException;
    String deleteCompany(String id) throws CompanyNotFoundException, RemoteApiAddressNotLoadedException;
    Company produceKafkaEventCompanyEdit(CompanyDto payload, String id) throws
            CompanyNotFoundException, CompanyTypeInvalidException, CompanyEmptyFieldsException, RemoteApiAddressNotLoadedException;
    Company editCompany(Company payload) throws RemoteApiAddressNotLoadedException;
}
