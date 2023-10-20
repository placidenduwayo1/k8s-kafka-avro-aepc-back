package com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.infra.adapters.input.controller;

import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.beans.address.Address;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.beans.employee.Employee;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.exceptions.*;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.ports.input.InputEmployeeService;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.ports.input.RemoteInputAddressService;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.infra.adapters.output.mapper.EmployeeMapper;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.infra.adapters.output.models.EmployeeDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

class EmployeeControllerTest {
    @Mock
    private InputEmployeeService inputEmployeeService;
    @Mock
    private RemoteInputAddressService remoteInputAddressService;
    @InjectMocks
    private EmployeeController underTest;
    private static final String FIRSTNAME = "Placide";
    private static final String LASTNAME = "Nduwayo";
    private static final String STATE = "active";
    private static final String TYPE = "software-engineer";
    private static final String ADDRESS_ID = "address-valenciennes";
    private Address address;
    private static final String EMPLOYEE_ID = "employee-id";
    private EmployeeDto dto;
    private Employee bean;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        address = new Address(ADDRESS_ID,184,"Avenue de Liège",59300,"Valenciennes","France");
        dto = EmployeeDto.builder()
                .firstname(FIRSTNAME)
                .lastname(LASTNAME)
                .state(STATE)
                .type(TYPE)
                .addressId(ADDRESS_ID)
                .build();
        bean = EmployeeMapper.fromDto(dto);
        bean.setAddress(address);
    }

    @Test
    void produceConsumeAndSaveEmployee() throws RemoteApiAddressNotLoadedException, EmployeeTypeInvalidException,
            EmployeeEmptyFieldsException, EmployeeStateInvalidException, EmployeeAlreadyExistsException {
        //PREPARE
        //EXECUTE
        Mockito.when(remoteInputAddressService.getRemoteAddressById(ADDRESS_ID)).thenReturn(address);
        Address actual1 = underTest.getRemoteAddress(ADDRESS_ID);
        Mockito.when(inputEmployeeService.produceKafkaEventEmployeeCreate(dto)).thenReturn(bean);
        Mockito.when(inputEmployeeService.createEmployee(bean)).thenReturn(new Employee());
        List<String> consumedAndSaved = underTest.produceConsumeAndSaveEmployee(dto);
        //VERIFY
        Assertions.assertAll("assertions",()->{
            Mockito.verify(remoteInputAddressService, Mockito.atLeast(1)).getRemoteAddressById(ADDRESS_ID);
            Mockito.verify(inputEmployeeService, Mockito.atLeast(1)).produceKafkaEventEmployeeCreate(dto);
            Mockito.verify(inputEmployeeService, Mockito.atLeast(1)).createEmployee(bean);
            Assertions.assertNotNull(actual1);
            Assertions.assertNotNull(consumedAndSaved);
            Assertions.assertEquals(2,consumedAndSaved.size());
        });
    }

    @Test
    void getRemoteAddress() throws RemoteApiAddressNotLoadedException {
        //PREPARE
        //EXECUTE
        Mockito.when(remoteInputAddressService.getRemoteAddressById(ADDRESS_ID)).thenReturn(address);
        Address actual = underTest.getRemoteAddress(ADDRESS_ID);
        //VERIFY
        Assertions.assertAll("assertions",()->{
            Mockito.verify(remoteInputAddressService, Mockito.atLeast(1)).getRemoteAddressById(ADDRESS_ID);
            Assertions.assertNotNull(actual);
            Assertions.assertEquals(address, actual);
        });
    }

    @Test
    void getRemoteAddresses() {
        //PREPARE
        List<Address> addresses = List.of(address,address,address);
        //EXECUTE
        Mockito.when(remoteInputAddressService.loadRemoteAllAddresses()).thenReturn(addresses);
        List<Address> actuals = underTest.getRemoteAddresses();
        //VERIFY
        Assertions.assertAll("assertions",()->{
            Mockito.verify(remoteInputAddressService, Mockito.atLeast(1)).loadRemoteAllAddresses();
            Assertions.assertNotNull(actuals);
            Assertions.assertEquals(addresses, actuals);
            Assertions.assertEquals(3, actuals.size());
            Assertions.assertTrue(actuals.contains(address));
        });
    }

    @Test
    void loadEmployeesOnGivenAddress() throws RemoteApiAddressNotLoadedException {
        //PREPARE
        List<Employee> beans = List.of(bean,bean,bean,bean);
        //EXECUTE
        Mockito.when(inputEmployeeService.loadEmployeesByRemoteAddress(ADDRESS_ID)).thenReturn(beans);
        List<Employee> actuals = underTest.loadEmployeesOnGivenAddress(ADDRESS_ID);
        //VERIFY
        Assertions.assertAll("assertions",()->{
            Mockito.verify(inputEmployeeService, Mockito.atLeast(1)).loadEmployeesByRemoteAddress(ADDRESS_ID);
            Assertions.assertNotNull(actuals);
            Assertions.assertEquals(beans, actuals);
            Assertions.assertEquals(4, actuals.size());
            Assertions.assertTrue(actuals.contains(bean));
        });
    }

    @Test
    void loadAllEmployees() {
        //PREPARE
        List<Employee> beans = List.of(bean,bean,bean,bean);
        //EXECUTE
        Mockito.when(inputEmployeeService.loadAllEmployees()).thenReturn(beans);
        List<Employee> actuals = underTest.loadAllEmployees();
        //VERIFY
        Assertions.assertAll("assertions",()->{
            Mockito.verify(inputEmployeeService, Mockito.atLeast(1)).loadAllEmployees();
            Assertions.assertNotNull(actuals);
            Assertions.assertEquals(beans, actuals);
            Assertions.assertEquals(4, actuals.size());
            Assertions.assertTrue(actuals.contains(bean));
        });
    }

    @Test
    void getEmployee() throws EmployeeNotFoundException, RemoteApiAddressNotLoadedException {
        //PREPARE
        //EXECUTE
        Mockito.when(inputEmployeeService.getEmployeeById(EMPLOYEE_ID)).thenReturn(Optional.of(bean));
        Employee actual = underTest.getEmployee(EMPLOYEE_ID);
        //VERIFY
        Assertions.assertAll("assertions",()->{
            Mockito.verify(inputEmployeeService, Mockito.atLeast(1)).getEmployeeById(EMPLOYEE_ID);
            Assertions.assertNotNull(actual);
            Assertions.assertEquals(bean, actual);
            Assertions.assertEquals(address,actual.getAddress());
        });
    }

    @Test
    void delete() throws EmployeeNotFoundException, RemoteApiAddressNotLoadedException {
        //PREPARE
        //EXECUTE
        Mockito.when(inputEmployeeService.produceKafkaEventEmployeeDelete(EMPLOYEE_ID)).thenReturn(bean);
        Mockito.when(inputEmployeeService.deleteEmployee(bean.getEmployeeId())).thenReturn("");
        ResponseEntity<Object> actual = underTest.delete(EMPLOYEE_ID);
        //VERIFY
        Assertions.assertAll("assertions",()->{
            Mockito.verify(inputEmployeeService, Mockito.atLeast(1)).produceKafkaEventEmployeeDelete(EMPLOYEE_ID);
            Mockito.verify(inputEmployeeService, Mockito.atLeast(1)).deleteEmployee(bean.getEmployeeId());
            Assertions.assertNotNull(actual);
        });
    }

    @Test
    void update() throws EmployeeTypeInvalidException, EmployeeEmptyFieldsException, EmployeeStateInvalidException,
            RemoteApiAddressNotLoadedException, EmployeeNotFoundException {
        //PREPARE
        Employee updated = bean;
        updated.setAddress(new Address("address-Paris",44,"Rue Notre Dame des Victoires",74002,"Paris","France"));
        //EXECUTE
        Mockito.when(inputEmployeeService.produceKafkaEventEmployeeEdit(dto,EMPLOYEE_ID)).thenReturn(updated);
        Mockito.when(inputEmployeeService.editEmployee(updated)).thenReturn(new Employee());
        List<String> consumedAndSaved = underTest.update(EMPLOYEE_ID,dto);
        //VERIFY
        Assertions.assertAll("assertions",()->{
            Mockito.verify(inputEmployeeService, Mockito.atLeast(1)).produceKafkaEventEmployeeEdit(dto,EMPLOYEE_ID);
            Mockito.verify(inputEmployeeService, Mockito.atLeast(1)).editEmployee(updated);
            Assertions.assertNotNull(consumedAndSaved);
            Assertions.assertEquals(2,consumedAndSaved.size());
        });
    }
}