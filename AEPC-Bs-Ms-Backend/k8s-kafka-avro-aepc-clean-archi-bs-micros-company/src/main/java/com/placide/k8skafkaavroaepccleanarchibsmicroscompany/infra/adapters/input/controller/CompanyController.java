package com.placide.k8skafkaavroaepccleanarchibsmicroscompany.infra.adapters.input.controller;

import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.ports.input.InputCompanyService;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.bean.Company;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.exceptions.CompanyAlreadyExistsException;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.exceptions.CompanyEmptyFieldsException;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.exceptions.CompanyNotFoundException;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.exceptions.CompanyTypeInvalidException;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.infra.adapters.output.models.CompanyDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class CompanyController {
    private final InputCompanyService companyService;

    public CompanyController(InputCompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping(value = "/companies")
    public List<String> produceConsumeAndSaveCompany(@RequestBody CompanyDto dto) throws
            CompanyEmptyFieldsException, CompanyAlreadyExistsException, CompanyTypeInvalidException {

        Company consumed = companyService.produceKafkaEventCompanyCreate(dto);
        Company saved = companyService.createCompany(consumed);
        return List.of("produced consumed:"+consumed,"saved:"+saved);
    }
    @GetMapping(value = "/companies")
    public List<Company> loadAllCompanies(){
       return companyService.loadAllCompanies();
    }
    @GetMapping(value = "/companies/{id}")
    public Optional<Company> loadCompany(@PathVariable(name = "id") String id) throws CompanyNotFoundException {
        return companyService.getCompanyById(id);
    }
    @PutMapping(value = "/companies/{id}")
    public ResponseEntity<Object> updateCompany(@RequestBody CompanyDto dto, @PathVariable(name = "id") String id) throws
            CompanyEmptyFieldsException, CompanyTypeInvalidException, CompanyNotFoundException {
        Company consumed = companyService.produceKafkaEventCompanyEdit(dto,id);
        Company saved = companyService.editCompany(consumed);
        return new ResponseEntity<>(String
                .format("<%s> to update is sent and consumed;%n <%s> is updated in db",
                        consumed, saved),
                HttpStatus.OK);
    }
    @DeleteMapping(value = "/companies/{id}")
    public ResponseEntity<Object> deleteCompany(@PathVariable(name = "id") String id) throws CompanyNotFoundException {
        Company consumed = companyService.produceKafkaEventCompanyDelete(id);
        companyService.deleteCompany(consumed.getCompanyId());
        return new ResponseEntity<>(String.format("<%s> to delete is sent to topic, %n <%s> is deleted from db",
                consumed, id), HttpStatus.OK);
    }
}
