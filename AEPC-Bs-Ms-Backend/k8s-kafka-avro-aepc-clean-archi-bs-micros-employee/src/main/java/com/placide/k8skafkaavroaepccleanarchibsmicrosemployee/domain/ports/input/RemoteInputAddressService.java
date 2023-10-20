package com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.ports.input;

import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.beans.address.Address;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.exceptions.RemoteApiAddressNotLoadedException;

import java.util.List;

public interface RemoteInputAddressService {
   Address getRemoteAddressById(String addressId) throws RemoteApiAddressNotLoadedException;
    List<Address> loadRemoteAllAddresses();
}
