package com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.infra.input.controller;

import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.domain.bean.Address;
import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.domain.exceptions.AddressAlreadyExistsException;
import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.domain.exceptions.AddressNotFoundException;
import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.domain.ports.input.InputAddressService;
import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.infra.adatpters.input.AddressController;
import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.infra.adatpters.output.models.AddressDto;
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
import java.util.UUID;

class AddressControllerTest {
    @Mock
    private InputAddressService inputAddressServiceMock;
    @InjectMocks
    private AddressController underTest;
    private final Address address = new Address(
            UUID.randomUUID().toString(),
            184, "Avenue de Liège",
            59300, "Valenciennes", "France");
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void produceAndConsumeAddress() throws AddressAlreadyExistsException {
        //PREPARE

        //EXECUTE
        Mockito.when(inputAddressServiceMock.produceAndConsumeAddressAdd(Mockito.any(AddressDto.class))).thenReturn(address);
        ResponseEntity<Object> response = underTest.produceAndConsumeAddress(Mockito.any(AddressDto.class));
        //VERIFY
        Assertions.assertAll("props",()->{
            Assertions.assertEquals(200,response.getStatusCode().value());
            Mockito.verify(inputAddressServiceMock, Mockito.atLeast(1)).produceAndConsumeAddressAdd(Mockito.any());
            Mockito.verify(inputAddressServiceMock, Mockito.atLeast(1)).saveInDbConsumedAddress(Mockito.any());
        });
    }

    @Test
    void getAllAddresses() {
        //PREPARE
        List<Address> addresses = List.of(
                new Address(UUID.randomUUID().toString(),184, "Avenue de Liège",59300, "Valenciennes","France"),
                new Address(UUID.randomUUID().toString(),44, "Rue Notre Dame des Victoires",74002,"Paris","France"),
                new Address(UUID.randomUUID().toString(),55, "Avenue Vendredi",91000,"Kibenga","Burundi"));
        //EXECUTE
        Mockito.when(inputAddressServiceMock.getAllAddresses()).thenReturn(addresses);
        List<Address> actualAddresses = underTest.getAllAddresses();
        //VERIFY
        Assertions.assertAll("props",()->{
            Assertions.assertEquals(addresses.size(), actualAddresses.size());
            Mockito.verify(inputAddressServiceMock, Mockito.atLeast(1)).getAllAddresses();
        });
    }

    @Test
    void getAddress() throws AddressNotFoundException {
        //PREPARE
        String addressId="uuid-001";
        Address expectedAddress = new Address(
                UUID.randomUUID().toString(),
                44, "Rue Notre Dame des Victoires",
                74002,"Paris","France");
        //EXECUTE
        Mockito.when(inputAddressServiceMock.getAddress(addressId)).thenReturn(Optional.of(expectedAddress));
        Address actualAddress = underTest.getAddress(addressId);
        //VERIFY
        Assertions.assertAll("props",()->{
            Mockito.verify(inputAddressServiceMock, Mockito.atLeast(1)).getAddress(addressId);
            Assertions.assertEquals(expectedAddress, actualAddress);
            Assertions.assertEquals(44, actualAddress.getNum());
            Assertions.assertSame(expectedAddress.getCity(), actualAddress.getCity());
        });
    }

    @Test
    void deleteAddress() throws AddressNotFoundException {
        //PREPARE
        String addressId="uuid-001";
        Address expectedAddress = new Address(
                UUID.randomUUID().toString(),
                44, "Rue Notre Dame des Victoires",
                74002,"Paris","France");
        //EXECUTE
        Mockito.when(inputAddressServiceMock.produceAndConsumeAddressDelete(addressId)).thenReturn(expectedAddress);
        ResponseEntity<Object> responseEntity = underTest.deleteAddress(addressId);
        //VERIFY
        Assertions.assertAll("props",()->{
            Assertions.assertEquals(200,responseEntity.getStatusCode().value());
            Mockito.verify(inputAddressServiceMock, Mockito.atLeast(1)).deleteAddress(expectedAddress.getAddressId());
        });
    }

    @Test
    void editAddress() throws AddressNotFoundException {
        //PREPARE
        String addressId="uuid-001";
        Address expectedAddress = new Address(
                UUID.randomUUID().toString(),
                44, "Rue Notre Dame des Victoires",
                74002,"Paris","France");
        Address consumedAddress = new Address(
                UUID.randomUUID().toString(),
                44, "Rue Notre Dame des Victoires",
                74002,"Paris","France");
        AddressDto addressDto = new AddressDto(
                44, "Rue Notre Dame des Victoires",
                74002,"Paris","France");
        //EXECUTE
        Mockito.when(inputAddressServiceMock.produceAndConsumeAddressEdit(addressDto,addressId)).thenReturn(consumedAddress);
        Mockito.when(inputAddressServiceMock.editAddress(consumedAddress)).thenReturn(expectedAddress);
        ResponseEntity<Object> response = underTest.editAddress(addressDto,addressId);

        //VERIFY
        Assertions.assertAll("props",()->{
            Assertions.assertEquals(200,response.getStatusCode().value());
            Mockito.verify(inputAddressServiceMock, Mockito.atLeast(1)).editAddress(consumedAddress);
        });
    }
}