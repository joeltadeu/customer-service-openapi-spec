package br.com.customer.web.controller;

import br.com.customer.openapi.api.EmailControllerApi;
import br.com.customer.openapi.model.*;
import br.com.customer.persistence.entity.Email;
import br.com.customer.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class EmailController implements EmailControllerApi, IController {
  private final EmailService service;
  private final ObjectMapper objectMapper;

  @Override
  public ResponseEntity<EmailResponse> createEmail(Long customerId, EmailRequest request) {
    var email = objectMapper.convertValue(request, Email.class);
    email.setCustomerId(customerId);
    var savedEmail = service.create(email);
    var emailResponse = objectMapper.convertValue(savedEmail, EmailResponse.class);

    return ResponseEntity.created(getURI(emailResponse.getId())).body(emailResponse);
  }

  @Override
  public ResponseEntity<Void> updateEmail(Long customerId, Long id, EmailRequest request) {
    var email = objectMapper.convertValue(request, Email.class);
    email.setId(id);
    email.setCustomerId(customerId);
    service.update(email);
    return ResponseEntity.ok().build();
  }

  @Override
  public ResponseEntity<EmailResponse> findEmailById(Long customerId, Long id) {

    final var email = service.findByCustomerIdAndId(customerId, id);
    final var emailResponse = objectMapper.convertValue(email, EmailResponse.class);

    return ResponseEntity.ok(emailResponse);
  }

  @Override
  public ResponseEntity<Void> deleteEmail(Long customerId, Long id) {
    var email = Email.builder().id(id).customerId(customerId).build();
    service.delete(email);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<PageEmailResponse> listAllEmails(
      @PathVariable Long customerId, EmailFilter filter) {

    Page<Email> emails = service.findAll(customerId, filter);

    var fetchedList =
        emails.stream()
            .map(email -> objectMapper.convertValue(email, EmailResponse.class))
            .collect(Collectors.toList());

    var page = new PageImpl<>(fetchedList, emails.getPageable(), emails.getTotalElements());
    var pageResponse = objectMapper.convertValue(page, PageEmailResponse.class);
    return ResponseEntity.ok(pageResponse);
  }
}
