package com.placide.k8skafkaavroaepccleanarchibsmicrosproject.infra.adapters.input.feignclients.proxies;

import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.infra.adapters.input.feignclients.fallback.CompanyServiceFallback;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.infra.adapters.input.feignclients.models.CompanyModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "k8s-kafka-avro-aepc-bs-ms-company", fallback = CompanyServiceFallback.class)
@Qualifier(value = "company-service-proxy")
public interface CompanyServiceProxy {
    @GetMapping(value = "/companies/{companyId}")
    Optional<CompanyModel> loadRemoteApiGetCompany(@PathVariable(name = "companyId") String companyId);
}
