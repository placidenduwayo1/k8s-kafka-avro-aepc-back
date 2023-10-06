package com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.domain.ports.input;

import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.domain.bean.Address;
import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.domain.exceptions.AddressAlreadyExistsException;
import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.domain.exceptions.AddressFieldsInvalidException;
import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.domain.exceptions.AddressNotFoundException;
import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.domain.exceptions.AddressCityNotFoundException;
import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.infra.adatpters.output.models.AddressDto;

import java.util.List;
import java.util.Optional;


public interface InputAddressService {
    Address produceAndConsumeAddressAdd(AddressDto addressDto) throws
            AddressFieldsInvalidException,
            AddressAlreadyExistsException;
    Address saveInDbConsumedAddress(Address address);
    List<Address> findAddressByInfo(AddressDto addressDto);
    List<Address> getAllAddresses();
    Optional<Address> getAddress(String addressID) throws AddressNotFoundException;
    List<Address> getAddressesOfGivenCity(String city) throws AddressCityNotFoundException;
    Address produceAndConsumeAddressDelete(String addressId) throws AddressNotFoundException;
    String deleteAddress (String addressId) throws AddressNotFoundException;
    Address produceAndConsumeAddressEdit(AddressDto payload, String addressId) throws
            AddressNotFoundException;
    Address editAddress(Address address);

}
