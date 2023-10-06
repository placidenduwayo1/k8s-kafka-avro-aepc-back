package com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.infra.adapters.input.feignclients.models;

import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressModel {
    private String addressId;
    private int num;
    private String street;
    private int poBox;
    private String city;
    private String country;
}
