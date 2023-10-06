package com.placide.k8skafkaavroaepccleanarchibsmicroscompany.infra.adapters.output.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name ="companies")
public class CompanyModel {
    @Id
    @GenericGenerator(name = "uuid")
    private String companyId;
    private String name;
    private String agency;
    private String type;
    private String connectedDate;
}
