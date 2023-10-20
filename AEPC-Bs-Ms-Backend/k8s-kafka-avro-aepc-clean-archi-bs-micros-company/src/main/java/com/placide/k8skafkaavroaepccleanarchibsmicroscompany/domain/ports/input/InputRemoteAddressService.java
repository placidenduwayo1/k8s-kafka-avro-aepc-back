package com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.ports.input;

import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.beans.address.Address;

public interface InputRemoteAddressService {
    Address getRemoteAddressById(String addressId);
}
