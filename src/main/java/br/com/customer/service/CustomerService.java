package br.com.customer.service;

import br.com.customer.openapi.model.CustomerFilter;
import br.com.customer.persistence.entity.Customer;
import br.com.customer.persistence.repository.AddressRepository;
import br.com.customer.persistence.repository.CustomerRepository;
import br.com.customer.persistence.repository.DocumentRepository;
import br.com.customer.persistence.repository.EmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerService {
  private final CustomerRepository customerRepository;
  private final AddressRepository addressRepository;
  private final EmailRepository emailRepository;
  private final DocumentRepository documentRepository;

  public Customer create(Customer customer) {
    return customerRepository.insert(customer);
  }

  public void update(Customer customer) {
    customerRepository.findById(customer.getId());
    customerRepository.update(customer);
  }

  public Customer findById(Long id) {
    var customer = customerRepository.findById(id);
    customer.setDocuments(documentRepository.findAllByCustomerId(id));
    customer.setAddresses(addressRepository.findAllByCustomerId(id));
    customer.setEmails(emailRepository.findAllByCustomerId(id));
    return customer;
  }

  public Page<Customer> findAll(CustomerFilter filter) {
    var customers = customerRepository.findAll(filter);
    var count = customerRepository.findAllCount(filter);

    return new PageImpl<>(
        customers, PageRequest.of(filter.getPageNumber(), filter.getPageSize()), count);
  }

  @Transactional
  public void delete(Long id) {
    customerRepository.findById(id);
    addressRepository.deleteByCustomerId(id);
    emailRepository.deleteByCustomerId(id);
    documentRepository.deleteByCustomerId(id);
    customerRepository.delete(id);
  }
}
