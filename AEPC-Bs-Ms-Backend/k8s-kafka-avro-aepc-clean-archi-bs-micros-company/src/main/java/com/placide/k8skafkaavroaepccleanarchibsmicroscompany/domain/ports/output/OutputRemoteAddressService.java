package com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.ports.output;

import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.beans.address.Address;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.exceptions.RemoteApiAddressNotLoadedException;

import java.util.Optional;

public interface OutputRemoteAddressService {
    Optional<Address> getRemoteAddressById(String addressId) throws RemoteApiAddressNotLoadedException;
}
