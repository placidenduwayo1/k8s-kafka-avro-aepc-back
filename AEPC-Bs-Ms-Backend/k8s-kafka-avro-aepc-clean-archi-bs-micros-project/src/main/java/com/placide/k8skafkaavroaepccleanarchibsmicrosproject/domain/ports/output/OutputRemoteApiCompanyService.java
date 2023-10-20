package com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.ports.output;

import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.beans.company.Company;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.exceptions.RemoteCompanyApiException;

public interface OutputRemoteApiCompanyService {
    Company getRemoteCompanyAPI(String companyId) throws RemoteCompanyApiException;
}
