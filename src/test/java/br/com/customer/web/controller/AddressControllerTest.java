package br.com.customer.web.controller;

import static br.com.customer.util.TestUtil.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.customer.Application;
import br.com.customer.config.MapperConfig;
import br.com.customer.openapi.model.AddressFilter;
import br.com.customer.persistence.entity.Address;
import br.com.customer.service.AddressService;
import br.com.customer.web.exception.DataNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest({AddressController.class})
@ContextConfiguration(classes = {Application.class, MapperConfig.class})
public class AddressControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private AddressService addressServiceMock;

  @Test
  public void createAddress_shouldCreate() throws Exception {

    final var addressId = 1L;
    final var customerId = 1L;
    final var address = getAddress(addressId);
    final var requestJson = getJsonFromObject(getAddressRequest());

    when(addressServiceMock.create(any(Address.class))).thenReturn(address);

    mockMvc
        .perform(
            post("/v1/customers/{customerId}/addresses", customerId)
                .contentType(APPLICATION_JSON)
                .content(requestJson))
        .andExpect(status().is(201))
        .andExpect(jsonPath("id").value(addressId));

    verify(addressServiceMock, times(1)).create(any(Address.class));
  }

  @Test
  public void updateAddress_shouldUpdate() throws Exception {

    final var addressId = 1L;
    final var customerId = 1L;
    final var requestJson = getJsonFromObject(getAddressRequest());

    mockMvc
        .perform(
            put("/v1/customers/{customerId}/addresses/{id}", customerId, addressId)
                .contentType(APPLICATION_JSON)
                .content(requestJson))
        .andExpect(status().is(200));

    verify(addressServiceMock, times(1)).update(any(Address.class));
  }

  @Test
  public void findAddressById_shouldFind() throws Exception {

    final var addressId = 1L;
    final var customerId = 1L;
    final var street = getRandomString();
    final var city = getRandomString();
    final var county = getRandomString();
    final var country = getRandomString();
    final var eircode = getRandomString();
    final var address = getAddress(addressId, street, city, county, country, eircode);

    when(addressServiceMock.findByCustomerIdAndId(customerId, addressId)).thenReturn(address);

    mockMvc
        .perform(
            get("/v1/customers/{customerId}/addresses/{id}", customerId, addressId)
                .contentType(APPLICATION_JSON))
        .andExpect(status().is(200))
        .andExpect(jsonPath("id").value(addressId))
        .andExpect(jsonPath("street").value(street))
        .andExpect(jsonPath("city").value(city))
        .andExpect(jsonPath("county").value(county))
        .andExpect(jsonPath("country").value(country))
        .andExpect(jsonPath("eircode").value(eircode));

    verify(addressServiceMock, times(1)).findByCustomerIdAndId(customerId, addressId);
  }

  @Test
  public void findAddressById_shouldThrowNotFoundWhenNotFound() throws Exception {
    final var addressId = getRandomLong();
    final var customerId = 1L;
    when(addressServiceMock.findByCustomerIdAndId(customerId, addressId))
        .thenThrow(
            new DataNotFoundException(
                "Address with id %s and customer id %s was not found"
                    .formatted(customerId, addressId)));

    mockMvc
        .perform(
            get("/v1/customers/{customerId}/addresses/{id}", customerId, addressId)
                .contentType(APPLICATION_JSON))
        .andExpect(status().is(404))
        .andExpect(
            jsonPath("description")
                .value(
                    "Address with id %s and customer id %s was not found"
                        .formatted(customerId, addressId)));

    verify(addressServiceMock, times(1)).findByCustomerIdAndId(customerId, addressId);
  }

  @Test
  public void deleteAddress_shouldDeleteAddress() throws Exception {
    final var addressId = getRandomLong();
    final var customerId = getRandomLong();

    doNothing().when(addressServiceMock).delete(any(Address.class));

    mockMvc
        .perform(
            delete("/v1/customers/{customerId}/addresses/{id}", customerId, addressId)
                .contentType(APPLICATION_JSON))
        .andExpect(status().is(204));

    verify(addressServiceMock, times(1)).delete(any(Address.class));
  }

  @Test
  public void deleteAddress_shouldThrowNotFoundWhenNotFound() throws Exception {
    final var addressId = getRandomLong();
    final var customerId = getRandomLong();

    doThrow(
            new DataNotFoundException(
                "Address with id %s and customer id %s was not found"
                    .formatted(customerId, addressId)))
        .when(addressServiceMock)
        .delete(any(Address.class));

    mockMvc
        .perform(
            delete("/v1/customers/{customerId}/addresses/{id}", customerId, addressId)
                .contentType(APPLICATION_JSON))
        .andExpect(status().is(404))
        .andExpect(
            jsonPath("description")
                .value(
                    "Address with id %s and customer id %s was not found"
                        .formatted(customerId, addressId)));

    verify(addressServiceMock, times(1)).delete(any(Address.class));
  }

  @Test
  public void findAddresses_shouldFind() throws Exception {
    final var customerId = getRandomLong();
    var filter = new AddressFilter();
    when(addressServiceMock.findAll(customerId, filter)).thenReturn(getPageAddress());

    mockMvc
        .perform(
            get("/v1/customers/{customerId}/addresses", customerId).contentType(APPLICATION_JSON))
        .andExpect(status().is(200));

    verify(addressServiceMock, times(1)).findAll(customerId, filter);
  }
}
