package com.placide.k8skafkaavroaepccleanarchibsmicroscompany.infra.adapters.input.feignclient.proxy;


import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.infra.adapters.input.feignclient.fallback.AddressServiceProxyFallback;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.infra.adapters.input.feignclient.model.AddressModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "k8s-kafka-avro-aepc-bs-ms-address", fallback = AddressServiceProxyFallback.class)
@Qualifier(value = "address-service-proxy")
public interface AddressServiceProxy {
    @GetMapping(value = "/addresses/id/{addressId}")
    AddressModel loadRemoteApiGetAddressById(@PathVariable String addressId);
}