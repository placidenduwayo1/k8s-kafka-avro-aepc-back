package com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.ports.input;

import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.beans.address.Address;

import java.util.Optional;

public interface InputRemoteAddressService {
    Optional<Address> getRemoteAddressById(String addressId);
}
