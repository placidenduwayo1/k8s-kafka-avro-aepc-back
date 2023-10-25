package com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.domain.usecase;

import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.infra.adatpters.output.models.AddressDto;

public class Validator {
    private Validator(){}
    public static boolean isInvalidAddress(AddressDto addressDto){
        return addressDto.getNum()<1
                || addressDto.getStreet().isBlank()
                || addressDto.getPoBox()<10000
                || addressDto.getCity().isBlank()
                || addressDto.getCountry().isBlank();
    }
    public static void formatAddress(AddressDto addressDto){
        addressDto.setStreet(addressDto.getStreet().strip());
        addressDto.setCity(addressDto.getCity().strip());
        addressDto.setCountry(addressDto.getCountry().strip());
    }
}
