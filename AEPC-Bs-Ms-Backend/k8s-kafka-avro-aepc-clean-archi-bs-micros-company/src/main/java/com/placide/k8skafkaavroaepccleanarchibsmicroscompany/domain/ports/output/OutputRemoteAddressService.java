package com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.ports.output;

import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.beans.address.Address;

public interface OutputRemoteAddressService {
    Address getRemoteAddressById(String addressId);
}
