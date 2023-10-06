package com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.domain.ports.output;

import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.domain.avrobean.AddressAvro;

public interface OutputKafkaProducerAddressService {
    AddressAvro sendKafkaAddressAddEvent(AddressAvro addressAvro);
    AddressAvro sendKafkaAddressDeleteEvent(AddressAvro addressAvro);
    AddressAvro sendKafkaAddressEditEvent(AddressAvro addressAvro);
}
