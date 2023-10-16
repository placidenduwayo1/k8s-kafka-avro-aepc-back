package com.placide.k8skafkaavroaepccleanarchibsmicroscompany.infra.adapters.output.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CompanyDto {
    private String name;
    private String agency;
    private String type;
    private String addressId;
}
