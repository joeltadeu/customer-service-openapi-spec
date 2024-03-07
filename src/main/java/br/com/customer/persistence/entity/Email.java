package br.com.customer.persistence.entity;

import java.io.Serializable;

import br.com.customer.persistence.entity.enums.EmailTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Email implements Serializable {
    private Long id;
    private Long customerId;
    private EmailTypeEnum type;
    private String email;
}
