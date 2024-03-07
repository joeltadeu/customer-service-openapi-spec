package br.com.customer.web.controller;

import br.com.customer.openapi.api.CustomerControllerApi;
import br.com.customer.openapi.model.CustomerFilter;
import br.com.customer.openapi.model.CustomerRequest;
import br.com.customer.openapi.model.CustomerResponse;
import br.com.customer.openapi.model.PageCustomerResponse;
import br.com.customer.persistence.entity.Customer;
import br.com.customer.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CustomerController implements CustomerControllerApi, IController {
  private final CustomerService service;
  private final ObjectMapper objectMapper;

  @Override
  public ResponseEntity<CustomerResponse> createCustomer(CustomerRequest request) {
    var customer = objectMapper.convertValue(request, Customer.class);
    var savedCustomer = service.create(customer);
    var customerResponse = objectMapper.convertValue(savedCustomer, CustomerResponse.class);

    return ResponseEntity.created(getURI(customerResponse.getId())).body(customerResponse);
  }

  @Override
  public ResponseEntity<Void> deleteCustomer(Long id) {
    service.delete(id);

    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<CustomerResponse> findCustomerById(Long id) {
    final var customer = service.findById(id);
    final var customerResponse = objectMapper.convertValue(customer, CustomerResponse.class);

    return ResponseEntity.ok(customerResponse);
  }

  @Override
  public ResponseEntity<PageCustomerResponse> listAllCustomers(CustomerFilter filter) {

    Page<Customer> customers = service.findAll(filter);

    var fetchedList =
        customers.stream()
            .map(customer -> objectMapper.convertValue(customer, CustomerResponse.class))
            .collect(Collectors.toList());

    var page = new PageImpl<>(fetchedList, customers.getPageable(), customers.getTotalElements());
    var pageResponse = objectMapper.convertValue(page, PageCustomerResponse.class);
    return ResponseEntity.ok(pageResponse);
  }

  @Override
  public ResponseEntity<Void> updateCustomer(Long id, CustomerRequest request) {
    var customer = objectMapper.convertValue(request, Customer.class);
    customer.setId(id);
    service.update(customer);

    return ResponseEntity.ok().build();
  }
}
