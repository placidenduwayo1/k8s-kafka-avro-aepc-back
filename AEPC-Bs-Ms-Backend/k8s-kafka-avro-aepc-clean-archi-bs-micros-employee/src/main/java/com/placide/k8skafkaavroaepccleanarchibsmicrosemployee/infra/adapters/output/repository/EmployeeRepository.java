package com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.infra.adapters.output.repository;

import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.infra.adapters.output.models.EmployeeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeModel,String> {
    List<EmployeeModel> findByFirstnameAndLastnameAndStateAndTypeAndAddressId(
            String firstname,String lastname, String state, String type, String addressId);
    List<EmployeeModel> findByAddressId(String addressId);
}
