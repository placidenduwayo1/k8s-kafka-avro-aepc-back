package com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.ports.output;

import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.avrobeans.CompanyAvro;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.exceptions.CompanyNotFoundException;

public interface OutputKafkaProducerService {
    CompanyAvro produceKafkaEventCompanyCreate(CompanyAvro companyAvro);
    CompanyAvro produceKafkaEventCompanyDelete(CompanyAvro companyAvro) throws CompanyNotFoundException;
    CompanyAvro produceKafkaEventCompanyEdit(CompanyAvro companyAvro) throws CompanyNotFoundException;
}
