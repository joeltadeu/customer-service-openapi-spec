package br.com.customer.service;

import static br.com.customer.util.TestUtil.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import br.com.customer.persistence.entity.Customer;
import br.com.customer.persistence.repository.AddressRepository;
import br.com.customer.persistence.repository.CustomerRepository;
import br.com.customer.persistence.repository.DocumentRepository;
import br.com.customer.persistence.repository.EmailRepository;
import br.com.customer.web.exception.DataNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

  @Mock CustomerRepository customerRepositoryMock;

  @Mock AddressRepository addressRepositoryMock;

  @Mock EmailRepository emailRepositoryMock;

  @Mock DocumentRepository documentRepositoryMock;

  @InjectMocks CustomerService customerService;

  @Captor private ArgumentCaptor<Customer> customerArgumentCaptor;

  @Test
  public void createCustomer_shouldCreateCustomer() {
    final var firstName = getRandomString();
    final var lastName = getRandomString();
    final var birthday = getRandomLocalDate();
    final var customerId = getRandomLong();

    final var incomeCustomer = getCustomer(null, firstName, lastName, birthday);
    final var outcomeCustomer = getCustomer(customerId, firstName, lastName, birthday);

    when(customerRepositoryMock.insert(customerArgumentCaptor.capture()))
        .thenReturn(outcomeCustomer);

    final var result = customerService.create(incomeCustomer);

    assertEquals(outcomeCustomer, result);

    final var argumentCaptorValue = customerArgumentCaptor.getValue();

    assertEquals(incomeCustomer.getFirstName(), argumentCaptorValue.getFirstName());
    assertEquals(incomeCustomer.getLastName(), argumentCaptorValue.getLastName());
    assertEquals(incomeCustomer.getBirthday(), argumentCaptorValue.getBirthday());
    assertNull(argumentCaptorValue.getId());

    verify(customerRepositoryMock, times(1)).insert(argumentCaptorValue);
  }

  @Test
  public void updateCustomer_shouldUpdateCustomer() {
    final var firstName = getRandomString();
    final var lastName = getRandomString();
    final var birthday = getRandomLocalDate();
    final var customerId = getRandomLong();

    final var customer = getCustomer(customerId, firstName, lastName, birthday);

    when(customerRepositoryMock.findById(customerId)).thenReturn(customer);

    customerService.update(customer);

    verify(customerRepositoryMock, times(1)).findById(customerId);
    verify(customerRepositoryMock, times(1)).update(customer);
  }

  @Test
  public void updateCustomer_shouldNotUpdateWhenCustomerNotFound() {
    final var firstName = getRandomString();
    final var lastName = getRandomString();
    final var birthday = getRandomLocalDate();
    final var customerId = getRandomLong();

    final var customer = getCustomer(customerId, firstName, lastName, birthday);

    when(customerRepositoryMock.findById(customerId))
        .thenThrow(
            new DataNotFoundException(
                String.format("Customer with id %s was not found", customerId)));

    final var assertThrows =
        assertThrows(DataNotFoundException.class, () -> customerService.update(customer));
    assertEquals(
        "Customer with id %s was not found".formatted(customerId), assertThrows.getMessage());
    verify(customerRepositoryMock, times(1)).findById(customerId);
    verify(customerRepositoryMock, times(0)).update(customer);
  }

  @Test
  public void deleteCustomerById_shouldDelete() {
    final var customerId = getRandomLong();
    final var foundCustomer = getCustomer(customerId);

    when(customerRepositoryMock.findById(customerId)).thenReturn(foundCustomer);

    doNothing().when(addressRepositoryMock).deleteByCustomerId(customerId);
    doNothing().when(emailRepositoryMock).deleteByCustomerId(customerId);
    doNothing().when(documentRepositoryMock).deleteByCustomerId(customerId);
    doNothing().when(customerRepositoryMock).delete(customerId);

    customerService.delete(customerId);

    verify(customerRepositoryMock, times(1)).findById(anyLong());
    verify(addressRepositoryMock, times(1)).deleteByCustomerId(anyLong());
    verify(emailRepositoryMock, times(1)).deleteByCustomerId(anyLong());
    verify(documentRepositoryMock, times(1)).deleteByCustomerId(anyLong());
    verify(customerRepositoryMock, times(1)).delete(anyLong());
  }

  @Test
  public void deleteCustomerById_shouldNotFound() {
    final var customerId = getRandomLong();

    when(customerRepositoryMock.findById(customerId))
        .thenThrow(
            new DataNotFoundException(
                String.format("Customer with id %s was not found", customerId)));

    final var assertThrows =
        assertThrows(DataNotFoundException.class, () -> customerService.delete(customerId));

    assertEquals(
        "Customer with id %s was not found".formatted(customerId), assertThrows.getMessage());

    verify(customerRepositoryMock, times(1)).findById(anyLong());
    verify(addressRepositoryMock, times(0)).deleteByCustomerId(anyLong());
    verify(emailRepositoryMock, times(0)).deleteByCustomerId(anyLong());
    verify(documentRepositoryMock, times(0)).deleteByCustomerId(anyLong());
    verify(customerRepositoryMock, times(0)).delete(anyLong());
  }

  @Test
  public void findCustomerById_shouldFind() {
    final var customerId = getRandomLong();
    final var addresses = getAddresses();
    final var emails = getEmails();
    final var documents = getDocuments();
    final var returnedCustomer = getCustomer(customerId, addresses, emails, documents);

    when(customerRepositoryMock.findById(customerId)).thenReturn(returnedCustomer);
    when(addressRepositoryMock.findAllByCustomerId(customerId)).thenReturn(addresses);
    when(emailRepositoryMock.findAllByCustomerId(customerId)).thenReturn(emails);
    when(documentRepositoryMock.findAllByCustomerId(customerId)).thenReturn(documents);

    final var customerReturnedFromDatabase = customerService.findById(customerId);

    assertEquals(returnedCustomer, customerReturnedFromDatabase);

    verify(customerRepositoryMock, times(1)).findById(anyLong());
    verify(addressRepositoryMock, times(1)).findAllByCustomerId(anyLong());
    verify(emailRepositoryMock, times(1)).findAllByCustomerId(anyLong());
    verify(documentRepositoryMock, times(1)).findAllByCustomerId(anyLong());
  }

  @Test
  public void findCustomerById_shouldNotFind() {
    final var customerId = getRandomLong();

    when(customerRepositoryMock.findById(customerId))
        .thenThrow(
            new DataNotFoundException(
                String.format("Customer with id %s was not found", customerId)));

    final var assertThrows =
        assertThrows(DataNotFoundException.class, () -> customerService.findById(customerId));

    assertEquals(
        "Customer with id %s was not found".formatted(customerId), assertThrows.getMessage());

    verify(customerRepositoryMock, times(1)).findById(anyLong());
    verify(addressRepositoryMock, times(0)).findAllByCustomerId(anyLong());
    verify(emailRepositoryMock, times(0)).findAllByCustomerId(anyLong());
    verify(documentRepositoryMock, times(0)).findAllByCustomerId(anyLong());
  }

  @Test
  public void findAllCustomers_shouldFind() {
    final var filter = getCustomerFilter();
    final var customers = getCustomers();

    when(customerRepositoryMock.findAll(filter)).thenReturn(customers);
    when(customerRepositoryMock.findAllCount(filter)).thenReturn(customers.size());

    final var pageCustomer = customerService.findAll(filter);

    assertEquals(customers.size(), pageCustomer.getTotalElements());
    assertEquals(1, pageCustomer.getTotalPages());
    assertEquals(10, pageCustomer.getSize());
    assertNotNull(pageCustomer.getContent());
    assertEquals(0, pageCustomer.getNumber());
    assertEquals(customers.size(), pageCustomer.getNumberOfElements());

    verify(customerRepositoryMock, times(1)).findAll(filter);
    verify(customerRepositoryMock, times(1)).findAllCount(filter);
  }
}
