package com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.infra.adatpters.output.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;

@Data
@AllArgsConstructor @NoArgsConstructor
@Entity
@Table(name = "addresses")
public class AddressModel implements Serializable {
    @Id
    @GenericGenerator(name = "uuid")
    private String addressId;
    private int num;
    private String street;
    private int poBox;
    private String city;
    private String country;
}
