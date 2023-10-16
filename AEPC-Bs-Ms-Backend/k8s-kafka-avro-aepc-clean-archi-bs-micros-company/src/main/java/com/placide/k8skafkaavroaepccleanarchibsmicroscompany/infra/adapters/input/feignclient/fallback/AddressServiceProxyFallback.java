package com.placide.k8skafkaavroaepccleanarchibsmicroscompany.infra.adapters.input.feignclient.fallback;

import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.infra.adapters.input.feignclient.model.AddressModel;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.infra.adapters.input.feignclient.proxy.AddressServiceProxy;
import org.springframework.stereotype.Component;

import java.util.Optional;

import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.exceptions.ExceptionMessage;

@Component
public class AddressServiceProxyFallback implements AddressServiceProxy {
    @Override
    public Optional<AddressModel> loadRemoteApiGetAddressById(String addressId) {
       return Optional.of(AddressModel.builder()
                .addressId(ExceptionMessage.ADDRESS_API_UNREACHABLE)
                .num(0)
                .street(ExceptionMessage.ADDRESS_API_UNREACHABLE)
                .poBox(0)
                .city(ExceptionMessage.ADDRESS_API_UNREACHABLE)
                .country(ExceptionMessage.ADDRESS_API_UNREACHABLE)
                .build());
    }
}
