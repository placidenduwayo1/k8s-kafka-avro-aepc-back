package com.placide.k8skafkaavroaepccleanarchibsmicroscompany.infra.adapters.input.controller;

import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.beans.address.Address;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.exceptions.*;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.ports.input.InputCompanyService;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.beans.company.Company;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.ports.input.InputRemoteAddressService;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.infra.adapters.output.models.CompanyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class CompanyController {
    private final InputCompanyService companyService;
    private final InputRemoteAddressService remoteAddressService;

    @Value("${personal.welcome.message}")
    private String welcome;

    @GetMapping(value = "")
    public ResponseEntity<Object> getWelcome(){
        return new ResponseEntity<>(welcome, HttpStatus.OK);
    }

    @PostMapping(value = "/companies")
    public List<String> produceConsumeAndSaveCompany(@RequestBody CompanyDto dto) throws
            CompanyEmptyFieldsException, CompanyAlreadyExistsException, CompanyTypeInvalidException, RemoteApiAddressNotLoadedException {

        Company consumed = companyService.produceKafkaEventCompanyCreate(dto);
        Company saved = companyService.createCompany(consumed);
       return List.of("produced & consumed:"+consumed,"saved:"+saved);
    }
    @GetMapping(value = "/companies")
    public List<Company> loadAllCompanies(){
       return companyService.loadAllCompanies();
    }
    @GetMapping(value = "/companies/{id}")
    public Optional<Company> loadCompany(@PathVariable(name = "id") String id) throws CompanyNotFoundException, RemoteApiAddressNotLoadedException {
        return companyService.getCompanyById(id);
    }
    @PutMapping(value = "/companies/{id}")
    public List<String> updateCompany(@RequestBody CompanyDto dto, @PathVariable(name = "id") String id) throws
            CompanyEmptyFieldsException, CompanyTypeInvalidException, CompanyNotFoundException, RemoteApiAddressNotLoadedException {
        Company consumed = companyService.produceKafkaEventCompanyEdit(dto,id);
        Company saved = companyService.editCompany(consumed);
        return List.of("produced & consumed:"+consumed,"saved:"+saved);
    }
    @DeleteMapping(value = "/companies/{id}")
    public ResponseEntity<Object> deleteCompany(@PathVariable(name = "id") String id) throws CompanyNotFoundException, RemoteApiAddressNotLoadedException {
        Company consumed = companyService.produceKafkaEventCompanyDelete(id);
        companyService.deleteCompany(consumed.getCompanyId());
        return new ResponseEntity<>(String.format("<%s> to delete is sent to topic, %n <%s> is deleted from db",
                consumed, id), HttpStatus.OK);
    }
    @GetMapping(value = "/companies/addresses/{addressId}")
    public Address getRemoteAddress(@PathVariable(name = "addressId") String addressId){
        return remoteAddressService.getRemoteAddressById(addressId);
    }
}
