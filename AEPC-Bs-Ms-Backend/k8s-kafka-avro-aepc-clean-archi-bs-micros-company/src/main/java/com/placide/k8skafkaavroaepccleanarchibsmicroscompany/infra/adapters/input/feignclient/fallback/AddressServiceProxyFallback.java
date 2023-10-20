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
       AddressModel resilience = new AddressModel();
       resilience.setAddressId(ExceptionMessage.ADDRESS_API_UNREACHABLE.getMessage());
       resilience.setNum(0);
       resilience.setStreet(ExceptionMessage.ADDRESS_API_UNREACHABLE.getMessage());
       resilience.setPoBox(0);
       resilience.setCity(ExceptionMessage.ADDRESS_API_UNREACHABLE.getMessage());
       resilience.setCountry(ExceptionMessage.ADDRESS_API_UNREACHABLE.getMessage());
       log.info("!!!!!!!!!!!!!!!!!![Fallback] resilience management {}",resilience);
       return resilience;
    }
}
