package com.placide.k8skafkaavroaepccleanarchibsmicroscompany.infra.adapters.input.feignclient.fallback;

import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.infra.adapters.input.feignclient.model.AddressModel;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.infra.adapters.input.feignclient.proxy.AddressServiceProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.exceptions.ExceptionMessage;

@Component
@Slf4j
public class AddressServiceProxyFallback implements AddressServiceProxy {
    @Override
    public AddressModel loadRemoteApiGetAddressById(String addressId) {
       AddressModel resilience = AddressModel.builder()
               .addressId(ExceptionMessage.ADDRESS_API_UNREACHABLE.getMessage())
               .num(0)
               .street(ExceptionMessage.ADDRESS_API_UNREACHABLE.getMessage())
               .poBox(0)
               .city(ExceptionMessage.ADDRESS_API_UNREACHABLE.getMessage())
               .country(ExceptionMessage.ADDRESS_API_UNREACHABLE.getMessage())
               .build();
       log.info("[Fallback] resilience management {}",resilience);
       return resilience;
    }
}
