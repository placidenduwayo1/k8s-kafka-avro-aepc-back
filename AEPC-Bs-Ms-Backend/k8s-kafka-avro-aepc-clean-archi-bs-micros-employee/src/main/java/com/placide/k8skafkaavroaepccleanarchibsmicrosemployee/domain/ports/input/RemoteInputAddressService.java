package com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.ports.input;

import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.beans.address.Address;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.exceptions.RemoteApiAddressNotLoadedException;

import java.util.List;
import java.util.Optional;

public interface RemoteInputAddressService {
    Optional<Address> getRemoteAddressById(String addressId) throws RemoteApiAddressNotLoadedException;
    List<Address> loadRemoteAllAddresses();
}
