package br.com.customer.persistence.entity;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Customer implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthday;
    private List<Address> addresses;
    private List<Email> emails;
    private List<Document> documents;
}
