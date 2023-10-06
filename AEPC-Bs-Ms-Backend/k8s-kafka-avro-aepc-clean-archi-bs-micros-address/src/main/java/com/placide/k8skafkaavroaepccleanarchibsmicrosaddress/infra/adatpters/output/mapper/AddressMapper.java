package com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.infra.adatpters.output.mapper;

import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.domain.bean.Address;
import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.infra.adatpters.output.models.AddressModel;
import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.infra.adatpters.output.models.AddressDto;
import com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.domain.avrobean.AddressAvro;
import org.springframework.beans.BeanUtils;

public class AddressMapper {
    private AddressMapper() {
    }

    public static Address mapModelToBean(AddressModel addressModel) {
        Address address = new Address();
        BeanUtils.copyProperties(addressModel, address);
        return address;
    }

    public static AddressModel mapBeanToModel(Address address) {
        AddressModel addressModel = new AddressModel();
        BeanUtils.copyProperties(address, addressModel);
        return addressModel;
    }

    public static Address mapDtoToBean(AddressDto addressDto) {
        Address address = new Address();
        BeanUtils.copyProperties(addressDto, address);
        return address;
    }

    public static AddressDto mapBeanToDto(Address address) {
        AddressDto addressDto = new AddressDto();
        BeanUtils.copyProperties(address, addressDto);
        return addressDto;
    }

    public static AddressAvro mapBeanToAvro(Address address) {
        return AddressAvro.newBuilder()
                .setAddressId(address.getAddressId())
                .setNum(address.getNum())
                .setStreet(address.getStreet())
                .setPoBox(address.getPoBox())
                .setCity(address.getCity())
                .setCountry(address.getCountry())
                .build();
    }

    public static Address mapAvroToBean(AddressAvro addressAvro) {
        return new Address(addressAvro.getAddressId(),
                addressAvro.getNum(),
                addressAvro.getStreet(),
                addressAvro.getPoBox(),
                addressAvro.getCity(),
                addressAvro.getCountry());
    }
}
