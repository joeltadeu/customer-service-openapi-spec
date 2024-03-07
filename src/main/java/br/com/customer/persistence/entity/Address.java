package br.com.customer.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address implements Serializable {
    private Long id;
    private Long customerId;
    private String street;
    private String complement;
    private String eircode;
    private String city;
    private String county;
    private String country;
}
