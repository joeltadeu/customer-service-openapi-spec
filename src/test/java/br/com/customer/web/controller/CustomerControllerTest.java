package br.com.customer.web.controller;

import br.com.customer.Application;
import br.com.customer.config.MapperConfig;
import br.com.customer.openapi.model.CustomerFilter;
import br.com.customer.persistence.entity.Customer;
import br.com.customer.service.CustomerService;
import br.com.customer.web.exception.DataNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static br.com.customer.util.TestUtil.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest({CustomerController.class})
@ContextConfiguration(classes = {Application.class, MapperConfig.class})
public class CustomerControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private CustomerService customerServiceMock;

  @Test
  public void createCustomer_shouldCreate() throws Exception {

    final var firstName = getRandomString();
    final var lastName = getRandomString();
    final var customerId = 1L;
    final var birthday = LocalDate.of(1980, 4, 13);
    final var customer = getCustomer(customerId, firstName, lastName, birthday);
    final var requestJson = getJsonFromObject(getCustomerRequest(firstName, lastName, birthday));

    when(customerServiceMock.create(any(Customer.class))).thenReturn(customer);

    mockMvc
        .perform(post("/v1/customers").contentType(APPLICATION_JSON).content(requestJson))
        .andExpect(status().is(201))
        .andExpect(jsonPath("id").value(customerId));

    verify(customerServiceMock, times(1)).create(any(Customer.class));
  }

  @Test
  public void updateCustomer_shouldUpdate() throws Exception {

    final var firstName = getRandomString();
    final var lastName = getRandomString();
    final var customerId = 1L;
    final var birthday = LocalDate.of(1980, 4, 13);
    final var requestJson = getJsonFromObject(getCustomerRequest(firstName, lastName, birthday));

    mockMvc.perform(put("/v1/customers/{id}", customerId).contentType(APPLICATION_JSON)
                    .content(requestJson))
            .andExpect(status().is(200));

    verify(customerServiceMock, times(1)).update(any(Customer.class));
  }

  @Test
  public void findCustomerById_shouldFind() throws Exception {

    final var firstName = getRandomString();
    final var lastName = getRandomString();
    final var customerId = 1L;
    final var birthday = LocalDate.of(1980, 4, 13);
    final var customer = getCustomer(customerId, firstName, lastName, birthday);

    when(customerServiceMock.findById(customerId)).thenReturn(customer);

    mockMvc.perform(get("/v1/customers/{id}", customerId).contentType(APPLICATION_JSON))
            .andExpect(status().is(200))
            .andExpect(jsonPath("id").value(customerId))
            .andExpect(jsonPath("firstName").value(firstName))
            .andExpect(jsonPath("lastName").value(lastName))
            .andExpect(jsonPath("birthday").value(birthday.toString()));

    verify(customerServiceMock, times(1)).findById(customerId);
  }

  @Test
  public void findCustomerById_shouldThrowNotFoundWhenNotFound() throws Exception {
    final var customerId = getRandomLong();
    when(customerServiceMock.findById(customerId)).thenThrow(
            new DataNotFoundException("Customer with Id %s was not found".formatted(customerId)));

    mockMvc.perform(get("/v1/customers/{id}", customerId).contentType(APPLICATION_JSON))
            .andExpect(status().is(404))
            .andExpect(jsonPath("description").value("Customer with Id %s was not found".formatted(customerId)));

    verify(customerServiceMock, times(1)).findById(customerId);
  }

  @Test
  public void deleteCustomer_shouldDeleteCustomer() throws Exception {
    final var customerId = getRandomLong();
    doNothing().when(customerServiceMock).delete(customerId);

    mockMvc.perform(delete("/v1/customers/{id}", customerId).contentType(APPLICATION_JSON))
            .andExpect(status().is(204));

    verify(customerServiceMock, times(1)).delete(customerId);
  }

  @Test
  public void deleteCustomer_shouldThrowNotFoundWhenNotFound() throws Exception {
    final var customerId = getRandomLong();
    doThrow(new DataNotFoundException("Customer with Id %s was not found".formatted(customerId)))
            .when(customerServiceMock)
            .delete(customerId);

    mockMvc.perform(delete("/v1/customers/{id}", customerId).contentType(APPLICATION_JSON))
            .andExpect(status().is(404))
            .andExpect(jsonPath("description").value("Customer with Id %s was not found".formatted(customerId)));

    verify(customerServiceMock, times(1)).delete(customerId);
  }

  @Test
  public void findCustomers_shouldFind() throws Exception {

    var filter = new CustomerFilter();
    when(customerServiceMock.findAll(filter)).thenReturn(getPageCustomer());

    mockMvc.perform(get("/v1/customers").contentType(APPLICATION_JSON))
            .andExpect(status().is(200));

    verify(customerServiceMock, times(1)).findAll(filter);
  }
}
