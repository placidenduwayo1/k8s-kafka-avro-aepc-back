package com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.infra.adapters.input.controller;

import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.beans.address.Address;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.beans.employee.Employee;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.exceptions.*;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.ports.input.InputEmployeeService;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.ports.input.RemoteInputAddressService;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.infra.adapters.output.models.EmployeeDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EmployeeController {
    private final InputEmployeeService inputEmployeeService;
    private final RemoteInputAddressService remoteInputAddressService;

    public EmployeeController(InputEmployeeService inputEmployeeService, RemoteInputAddressService remoteInputAddressService) {
        this.inputEmployeeService = inputEmployeeService;
        this.remoteInputAddressService = remoteInputAddressService;
    }

    @PostMapping(value = "/employees")
    public List<String> produceConsumeAndSaveEmployee(@RequestBody EmployeeDto employeeDto) throws
            EmployeeTypeInvalidException, EmployeeEmptyFieldsException, EmployeeStateInvalidException,
            RemoteApiAddressNotLoadedException, EmployeeAlreadyExistsException {

       Employee consumed = inputEmployeeService.produceKafkaEventEmployeeCreate(employeeDto);
       Employee saved = inputEmployeeService.createEmployee(consumed);
        return List.of("produced consumed:"+consumed,"saved:"+saved);
    }
    @GetMapping(value = "/employees/addresses/id/{addressId}")
    public Address getRemoteAddress(@PathVariable(name = "addressId") String addressId) throws
            RemoteApiAddressNotLoadedException{
        return remoteInputAddressService.getRemoteAddressById(addressId)
                .orElseThrow(RemoteApiAddressNotLoadedException::new);
    }
    @GetMapping(value = "/employees/addresses")
    public List<Address> getRemoteAddresses(){
       return remoteInputAddressService.loadRemoteAllAddresses();
    }
    @GetMapping(value = "/employees/addresses/{addressId}")
    public List<Employee> loadEmployeesOnGivenAddress(@PathVariable(name = "addressId") String addressId) throws
            RemoteApiAddressNotLoadedException {
        List<Employee> employees = inputEmployeeService.loadEmployeesByRemoteAddress(addressId);
        return setAddressToEmployee(employees);
    }
    @GetMapping(value = "/employees")
    public List<Employee> loadAllEmployees(){
        List<Employee> employees = inputEmployeeService.loadAllEmployees();
        return setAddressToEmployee(employees);
    }
    @GetMapping(value = "/employees/{id}")
    public Employee getEmployee(@PathVariable(name = "id") String id) throws EmployeeNotFoundException {
        return inputEmployeeService.getEmployeeById(id).orElseThrow(EmployeeNotFoundException::new);
    }
    @DeleteMapping(value = "/employees/{id}")
    public ResponseEntity<Object> delete(@PathVariable(name = "id") String id) throws EmployeeNotFoundException, RemoteApiAddressNotLoadedException {
        Employee consumed = inputEmployeeService.produceKafkaEventEmployeeDelete(id);
        inputEmployeeService.deleteEmployee(consumed.getEmployeeId());
        return new ResponseEntity<>(String
                .format("<%s> to delete is sent to topic, %n <%s> is deleted from db", consumed, id),
                HttpStatus.OK);
    }
    @PutMapping(value = "/employees/{id}")
    public List<String> update(@PathVariable(name = "id") String id, @RequestBody EmployeeDto dto) throws EmployeeTypeInvalidException,
            EmployeeEmptyFieldsException, EmployeeStateInvalidException, RemoteApiAddressNotLoadedException, EmployeeNotFoundException {
        Employee consumed = inputEmployeeService.produceKafkaEventEmployeeEdit(dto,id);
        Employee saved = inputEmployeeService.editEmployee(consumed);
        return List.of("produced consumed:"+consumed,"saved:"+saved);
    }
    private List<Employee> setAddressToEmployee(List<Employee> employees){
        employees.forEach((var employee)->{
            try {
                Address address = remoteInputAddressService.getRemoteAddressById(employee.getAddressId())
                        .orElseThrow(RemoteApiAddressNotLoadedException::new);
                employee.setAddress(address);
            } catch (RemoteApiAddressNotLoadedException e) {
              e.getMessage();
            }
        });
        return employees;
    }
}
