package com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.domain.usecase;

import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.domain.avrobean.AddressAvro;
import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.domain.bean.Address;
import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.domain.exceptions.AddressAlreadyExistsException;
import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.domain.exceptions.AddressCityNotFoundException;
import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.domain.exceptions.AddressNotFoundException;
import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.domain.ports.output.OutputKafkaProducerAddressService;
import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.domain.ports.output.OutputAddressService;
import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.infra.adatpters.output.mapper.AddressMapper;
import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.infra.adatpters.output.models.AddressDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


class UseCaseTest {
    @Mock
    private OutputAddressService outputAddressServiceMock;
    @Mock
    private OutputKafkaProducerAddressService kafkaProducerAddressServiceMock;
    @InjectMocks
    private UseCase underTest;
    private Address address;
    private AddressAvro addressAvro;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        address = new Address(
                UUID.randomUUID().toString(),
                184, "Avenue de Liège",
                59300, "Valenciennes", "France"
        );
        addressAvro = AddressMapper.mapBeanToAvro(address);
    }

    @Test
    void produceAndConsumeAddress() throws AddressAlreadyExistsException {
        AddressDto addressDto = new AddressDto(
                184, "Avenue de Liège",
                59300, "Valenciennes", "France");
        Mockito.when(kafkaProducerAddressServiceMock.sendKafkaAddressAddEvent(Mockito.any(AddressAvro.class)))
                .thenReturn(addressAvro);
        Address consumedAddress = underTest.produceAndConsumeAddressAdd(addressDto);

        Assertions.assertAll("gpe of assertions",
                () -> Mockito.verify(kafkaProducerAddressServiceMock, Mockito.atLeast(1))
                        .sendKafkaAddressAddEvent(Mockito.any(AddressAvro.class)),
                () -> Assertions.assertNotNull(consumedAddress));
    }

    @Test
    void saveInDbConsumedAddress() {
        Address newAddress = new Address(
                address.getAddressId(),
                184, "Avenue de Liège",
                59300, "Valenciennes", "France");
        Mockito.when(outputAddressServiceMock.saveInDbConsumedAddress(newAddress))
                .thenReturn(address);
        Address savedAddress = underTest.saveInDbConsumedAddress(newAddress);
        Assertions.assertAll("props", () -> {
            Mockito.verify(outputAddressServiceMock, Mockito.atLeast(1)).saveInDbConsumedAddress(newAddress);
            Assertions.assertEquals(address, savedAddress);
        });
    }

    @Test
    void findAddressByInfo() {
        AddressDto addressDto = new AddressDto(
                1, "rue_test", 10000, "city-test", "country-test");
        List<Address> addresses = List.of();
        Mockito.when(outputAddressServiceMock.findAddressByInfo(addressDto)).thenReturn(addresses);
        List<Address> actual = underTest.findAddressByInfo(addressDto);
        Assertions.assertAll("props", () -> {
            Mockito.verify(outputAddressServiceMock, Mockito.atLeast(1)).findAddressByInfo(addressDto);
            Assertions.assertEquals(0, actual.size());
        });
    }

    @Test
    void getAllAddresses() {
        List<Address> addresses = List.of(address, address, address);
        Mockito.when(outputAddressServiceMock.getAllAddresses()).thenReturn(addresses);
        List<Address> actualAddresses = underTest.getAllAddresses();
        Assertions.assertAll("props", () -> {
            Mockito.verify(outputAddressServiceMock, Mockito.atLeast(1)).getAllAddresses();
            Assertions.assertEquals(3, actualAddresses.size());
        });
    }

    @Test
    void getAddress() throws AddressNotFoundException {
        Address newAddress = new Address(
                UUID.randomUUID().toString(),
                184, "Avenue de Liège",
                59300, "Valenciennes",
                "France");
        Mockito.when(outputAddressServiceMock.getAddress("1L")).thenReturn(Optional.of(newAddress));
        Optional<Address> foundAddress = underTest.getAddress("1L");
        Assertions.assertAll("props", () -> {
            Mockito.verify(outputAddressServiceMock, Mockito.atLeast(1)).getAddress("1L");
            Assertions.assertNotNull(foundAddress);
        });
    }

    @Test
    void produceAndConsumeAddressId() throws AddressNotFoundException {
        String addressId = UUID.randomUUID().toString();
        Mockito.when(kafkaProducerAddressServiceMock.sendKafkaAddressDeleteEvent(addressAvro)).thenReturn(addressAvro);
        Mockito.when(outputAddressServiceMock.getAddress(addressId)).thenReturn(Optional.of(address));
        Address actual = underTest.produceAndConsumeAddressDelete(addressId);
        Assertions.assertAll("props", () -> {
            Mockito.verify(kafkaProducerAddressServiceMock, Mockito.atLeast(1)).sendKafkaAddressDeleteEvent(addressAvro);
            Assertions.assertEquals(address.getAddressId(), actual.getAddressId());
            Assertions.assertEquals(address.getNum(), actual.getNum());
            Assertions.assertEquals(address.getStreet(), actual.getStreet());
            Assertions.assertEquals(address.getPoBox(), actual.getPoBox());
            Assertions.assertEquals(address.getCity(), actual.getCity());
            Assertions.assertEquals(address.getCountry(), actual.getCountry());
        });
    }

    @Test
    void deleteAddress() throws AddressNotFoundException {
        //PREPARE
        String addressId = UUID.randomUUID().toString();
        //EXECUTE
        Mockito.when(outputAddressServiceMock.getAddress(addressId))
                .thenReturn(Optional.of(address));
        Address actual = underTest.getAddress(addressId).orElseThrow(AddressNotFoundException::new);
        Mockito.when(outputAddressServiceMock.deleteAddress(addressId))
                .thenReturn("address " + address + " successfully deleted");

        String actualMessage = underTest.deleteAddress(addressId);
        //VERIFY

        Assertions.assertAll("props",()->{
            Mockito.verify(outputAddressServiceMock, Mockito.atLeast(1)).deleteAddress(address.getAddressId());
            Mockito.verify(outputAddressServiceMock, Mockito.atLeast(1)).getAddress(addressId);
            Assertions.assertNotNull(actualMessage);
            Assertions.assertEquals(address.getAddressId(), actual.getAddressId());
            Assertions.assertEquals(address.getNum(), actual.getNum());
            Assertions.assertEquals(address.getStreet(), actual.getStreet());
            Assertions.assertEquals(address.getPoBox(), actual.getPoBox());
            Assertions.assertEquals(address.getCity(), actual.getCity());
            Assertions.assertEquals(address.getCountry(), actual.getCountry());
        });
    }

    @Test
    void produceAndConsumeAddressEdit() throws AddressNotFoundException {
        //PREPARE
        AddressDto addressDto = new AddressDto(
                184, "Avenue de Liège",
                59300, "Valenciennes", "France");
        String id = "uuid-0001";
        //EXECUTE
        Mockito.when(kafkaProducerAddressServiceMock.sendKafkaAddressEditEvent(addressAvro))
                .thenReturn(addressAvro);
        Mockito.when(outputAddressServiceMock.getAddress(id)).thenReturn(Optional.of(address));
        Address actual = underTest.produceAndConsumeAddressEdit(addressDto, id);
        //VERIFY
        Assertions.assertAll("prop", () -> {
            Assertions.assertEquals(address.getAddressId(), actual.getAddressId());
            Assertions.assertEquals(address.getNum(), actual.getNum());
            Assertions.assertEquals(address.getStreet(), actual.getStreet());
            Assertions.assertEquals(address.getPoBox(), actual.getPoBox());
            Assertions.assertEquals(address.getCity(), actual.getCity());
            Assertions.assertEquals(address.getCountry(), actual.getCountry());
            Mockito.verify(kafkaProducerAddressServiceMock, Mockito.atLeast(1))
                    .sendKafkaAddressEditEvent(addressAvro);
        });
    }

    @Test
    void editAddress() {
        // PREPARE
        Address updatedAddress = new Address(
                UUID.randomUUID().toString(),
                10, "Avenue de Liège",
                59300, "Valenciennes", "France");
        //EXECUTE
        Mockito.when(outputAddressServiceMock.updateAddress(address)).thenReturn(updatedAddress);
        Address actual = underTest.editAddress(address);
        Assertions.assertAll("props",()->{
            Assertions.assertEquals(updatedAddress.getAddressId(), actual.getAddressId());
            Assertions.assertEquals(updatedAddress.getNum(), actual.getNum());
            Assertions.assertEquals(updatedAddress.getStreet(), actual.getStreet());
            Assertions.assertEquals(updatedAddress.getPoBox(), actual.getPoBox());
            Assertions.assertEquals(updatedAddress.getCity(), actual.getCity());
            Assertions.assertEquals(updatedAddress.getCountry(), actual.getCountry());
            Mockito.verify(outputAddressServiceMock, Mockito.atLeast(1)).updateAddress(address);
        });
    }

    @Test
    void getAddressesOfGivenCity() throws AddressCityNotFoundException {
        //PREPARE
        String city = "Valenciennes";
        List<Address> addresses = List.of(address, address, address);
        //EXECUTE
        Mockito.when(outputAddressServiceMock.getAddressesOfGivenCity(city)).thenReturn(addresses);
        List<Address> obtained = underTest.getAddressesOfGivenCity(city);
        //VERIFY
        Assertions.assertAll("gpe of assertions",
                () -> Mockito.verify(outputAddressServiceMock).getAddressesOfGivenCity(city),
                () -> Assertions.assertFalse(obtained.isEmpty()));
    }
}