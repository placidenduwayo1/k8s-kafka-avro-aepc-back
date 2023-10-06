package com.placide.k8skafkaavroaepccleanarchibsmicroscompany.infra.adapters.output.repository;

import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.infra.adapters.output.models.CompanyModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyRepository extends JpaRepository<CompanyModel,String> {
    List<CompanyModel> findByNameAndAgencyAndType(String name, String agency, String type);
}
