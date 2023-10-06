package com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.ports.input;

import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.beans.company.Company;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.exceptions.RemoteCompanyApiException;

import java.util.Optional;

public interface InputRemoteApiCompanyService {
    Optional<Company> getRemoteApiCompany(String companyId) throws RemoteCompanyApiException;
}
