package com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.ports.input;

import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.beans.address.Address;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.exceptions.RemoteApiAddressNotLoadedException;

import java.util.Optional;

public interface InputRemoteAddressService {
    Optional<Address> getRemoteAddressById(String addressId) throws RemoteApiAddressNotLoadedException;
}
