package br.com.customer.service;

import br.com.customer.openapi.model.EmailFilter;
import br.com.customer.persistence.entity.Email;
import br.com.customer.persistence.repository.EmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
  private final EmailRepository emailRepository;

  public Email create(Email email) {
    return emailRepository.insert(email);
  }

  public void update(Email email) {
    emailRepository.findByCustomerIdAndId(email.getCustomerId(), email.getId());
    emailRepository.update(email);
  }

  public Email findByCustomerIdAndId(Long customerId, Long id) {
    return emailRepository.findByCustomerIdAndId(customerId, id);
  }

  public Page<Email> findAll(Long customerId, EmailFilter filter) {
    var emails = emailRepository.findAll(customerId, filter);
    var count = emailRepository.findAllCount(customerId, filter);

    return new PageImpl<>(
        emails, PageRequest.of(filter.getPageNumber(), filter.getPageSize()), count);
  }

  public void delete(Email email) {
    emailRepository.findByCustomerIdAndId(email.getCustomerId(), email.getId());
    emailRepository.delete(email.getId());
  }
}
