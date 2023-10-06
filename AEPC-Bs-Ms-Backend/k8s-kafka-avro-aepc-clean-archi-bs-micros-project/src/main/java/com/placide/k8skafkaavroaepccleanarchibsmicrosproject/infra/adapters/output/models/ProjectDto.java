package com.placide.k8skafkaavroaepccleanarchibsmicrosproject.infra.adapters.output.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectDto {
    private String name;
    private String description;
    private int priority;
    private String state;
    private String employeeId;
    private String companyId;
}
