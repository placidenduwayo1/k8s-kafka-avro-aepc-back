package com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.infra.adapters.input.feignclients.proxy;

import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.exceptions.RemoteApiAddressNotLoadedException;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.infra.adapters.input.feignclients.models.AddressModel;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.infra.adapters.input.feignclients.fallback.AddressServiceProxyFallback;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "k8s-kafka-avro-aepc-bs-ms-address", fallback = AddressServiceProxyFallback.class)
@Qualifier(value = "address-service-proxy")
public interface AddressServiceProxy {
    @GetMapping(value = "/addresses/id/{addressId}")
   AddressModel loadRemoteApiGetAddressById(@PathVariable String addressId) throws RemoteApiAddressNotLoadedException;
    @GetMapping(value = "/addresses")
    List<AddressModel> loadRemoteAddressIpiGetAllAddresses();
}