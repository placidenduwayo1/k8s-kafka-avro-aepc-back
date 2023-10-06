package com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.ports.output;

import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.beans.address.Address;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.exceptions.RemoteApiAddressNotLoadedException;

import java.util.List;
import java.util.Optional;

public interface OutputRemoteAddressService {
    Optional<Address> getRemoteAddressById(String addressId) throws RemoteApiAddressNotLoadedException;
    List<Address> loadAllRemoteAddresses();
}
