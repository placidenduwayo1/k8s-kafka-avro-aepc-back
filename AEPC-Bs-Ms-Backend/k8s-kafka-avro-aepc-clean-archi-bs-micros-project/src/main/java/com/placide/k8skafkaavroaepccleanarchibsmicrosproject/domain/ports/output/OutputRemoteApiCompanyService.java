package com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.ports.output;

import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.beans.company.Company;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.exceptions.RemoteCompanyApiException;

import java.util.Optional;

public interface OutputRemoteApiCompanyService {
    Optional<Company> getRemoteCompanyAPI(String companyId) throws RemoteCompanyApiException;
}
