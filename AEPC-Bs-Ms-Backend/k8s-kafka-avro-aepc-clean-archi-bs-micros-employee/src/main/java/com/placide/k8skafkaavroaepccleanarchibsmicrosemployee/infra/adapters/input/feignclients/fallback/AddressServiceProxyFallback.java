package com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.infra.adapters.input.feignclients.fallback;

import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.infra.adapters.input.feignclients.models.AddressModel;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.infra.adapters.input.feignclients.proxy.AddressServiceProxy;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.exceptions
        .ExceptionsMsg.ADDRESS_API_UNREACHABLE;
@Component
public class AddressServiceProxyFallback implements AddressServiceProxy {
    @Override
    public AddressModel loadRemoteApiGetAddressById(String addressId) {
       return AddressModel.builder()
                .addressId(ADDRESS_API_UNREACHABLE.getMessage())
                .num(0)
                .street(ADDRESS_API_UNREACHABLE.getMessage())
                .poBox(0)
                .city(ADDRESS_API_UNREACHABLE.getMessage())
                .country(ADDRESS_API_UNREACHABLE.getMessage())
                .build();
    }
    @Override
    public List<AddressModel> loadRemoteAddressIpiGetAllAddresses() {
        return List.of(AddressModel.builder()
                .addressId(ADDRESS_API_UNREACHABLE.getMessage())
                .num(0)
                .street(ADDRESS_API_UNREACHABLE.getMessage())
                .poBox(0)
                .city(ADDRESS_API_UNREACHABLE.getMessage())
                .country(ADDRESS_API_UNREACHABLE.getMessage())
                .build());
    }
}
