package com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.domain.usecase;

import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.domain.avrobean.AddressAvro;
import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.domain.bean.Address;
import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.domain.exceptions.AddressAlreadyExistsException;
import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.domain.exceptions.AddressCityNotFoundException;
import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.domain.exceptions.AddressFieldsInvalidException;
import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.domain.exceptions.AddressNotFoundException;
import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.domain.ports.input.InputAddressService;
import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.domain.ports.output.OutputKafkaProducerAddressService;
import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.domain.ports.output.OutputAddressService;
import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.infra.adatpters.output.mapper.AddressMapper;
import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.infra.adatpters.output.models.AddressDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.UnaryOperator;

public class UseCase implements InputAddressService {
    private final OutputAddressService outputAddressService;
    private final OutputKafkaProducerAddressService outputKafkaProducerAddressService;

    public UseCase(
            OutputKafkaProducerAddressService outputKafkaProducerAddressService,
            OutputAddressService outputAddressService) {

        this.outputKafkaProducerAddressService = outputKafkaProducerAddressService;
        this.outputAddressService = outputAddressService;
    }

    private void checkAddressValidity(AddressDto addressDto) throws AddressFieldsInvalidException{
        if (Validator.isInvalidAddress(addressDto)) {
            throw new AddressFieldsInvalidException();
        }
    }
    private void checkAddressAlreadyExists(AddressDto addressDto) throws AddressAlreadyExistsException {
        if (!findAddressByInfo(addressDto).isEmpty()){
            throw new AddressAlreadyExistsException();
        }
    }

    /*send address event to kafka topic and consumer consumes it*/
    @Override
    public Address produceAndConsumeAddressAdd(AddressDto addressDto) throws
            AddressFieldsInvalidException, AddressAlreadyExistsException {

        Validator.formatAddress(addressDto);
        checkAddressValidity(addressDto);
        checkAddressAlreadyExists(addressDto);
        Address address = AddressMapper.mapDtoToBean(addressDto);
        address.setAddressId(UUID.randomUUID().toString());
        AddressAvro addressAvro = AddressMapper.mapBeanToAvro(address);
        return AddressMapper.mapAvroToBean(outputKafkaProducerAddressService.sendKafkaAddressAddEvent(addressAvro));
    }

    @Override
    public Address saveInDbConsumedAddress(Address address) {
        return outputAddressService.saveInDbConsumedAddress(address);
    }

    @Override
    public List<Address> findAddressByInfo(AddressDto addressDto) {
        return outputAddressService.findAddressByInfo(addressDto);
    }

    @Override
    public List<Address> getAllAddresses() {
        return outputAddressService.getAllAddresses();
    }

    @Override
    public Optional<Address> getAddress(String addressID) throws AddressNotFoundException {
       Address address = outputAddressService.getAddress(addressID).orElseThrow(
               AddressNotFoundException::new
       );

        return Optional.of(address);
    }
    @Override
    public List<Address> getAddressesOfGivenCity(String city) throws AddressCityNotFoundException {

        List<Address> addresses = outputAddressService.getAddressesOfGivenCity(city);
        for(Address address : addresses){
            if(!address.getCity().equals(city)){
                throw new AddressCityNotFoundException();
            }
        }

        UnaryOperator<Address> mapper = (var address)->{
            address.setAddressId("<"+address.getAddressId()+">");
            address.setStreet("<"+address.getStreet()+">");
            address.setCity("<"+city+">");
            address.setCountry("<"+address.getCountry()+">");
            return address;
        };

        return addresses.stream()
                .map(mapper)
                .toList();
    }
    /*send addressId event to kafka topic and consumer consumes it*/
    @Override
    public Address produceAndConsumeAddressDelete(String addressId) throws AddressNotFoundException {
        Address address = getAddress(addressId).orElseThrow(AddressNotFoundException::new);
        AddressAvro addressAvro = AddressMapper.mapBeanToAvro(address);
        return AddressMapper.mapAvroToBean(outputKafkaProducerAddressService.sendKafkaAddressDeleteEvent(addressAvro));
    }
    @Override
    public String deleteAddress(String addressId) throws AddressNotFoundException {
       Address address = getAddress(addressId).orElseThrow(AddressNotFoundException::new);
        outputAddressService.deleteAddress(address.getAddressId());
        return "Address <"+ address +"> successfully deleted";
    }
    @Override
    public Address produceAndConsumeAddressEdit(AddressDto payload, String addressId) throws AddressNotFoundException {
        Validator.formatAddress(payload);
        checkAddressValidity(payload);
        Address address = getAddress(addressId).orElseThrow(AddressNotFoundException::new);
        AddressAvro addressAvro = AddressMapper.mapBeanToAvro(address);
        return AddressMapper.mapAvroToBean(outputKafkaProducerAddressService.sendKafkaAddressEditEvent(addressAvro));
    }
    @Override
    public Address editAddress(Address address) {
        return outputAddressService.updateAddress(address);
    }

}
