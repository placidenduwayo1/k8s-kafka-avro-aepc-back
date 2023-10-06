package com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.domain.ports.output;

import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.domain.bean.Address;
import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.domain.exceptions.AddressCityNotFoundException;
import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.domain.exceptions.AddressNotFoundException;
import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.infra.adatpters.output.models.AddressDto;
import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.domain.avrobean.AddressAvro;

import java.util.List;
import java.util.Optional;

public interface OutputAddressService {
    Address consumeAddressAdd(AddressAvro addressAvro, String topic);
    Address saveInDbConsumedAddress(Address address);
    List<Address> findAddressByInfo(AddressDto addressDto);
    List<Address> getAllAddresses();
   Optional<Address> getAddress(String addressID) throws AddressNotFoundException;
    List<Address> getAddressesOfGivenCity(String city) throws AddressCityNotFoundException;
   Address consumeAddressDelete(AddressAvro addressAvro, String topic);
   String deleteAddress(String addressId) throws AddressNotFoundException;
    Address consumeAddressEdit(AddressAvro addressAvro, String topic);
    Address updateAddress(Address address);
}
