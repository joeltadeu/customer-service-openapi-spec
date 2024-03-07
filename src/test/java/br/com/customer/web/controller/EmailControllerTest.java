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
import br.com.customer.openapi.model.EmailFilter;
import br.com.customer.persistence.entity.Email;
import br.com.customer.persistence.entity.enums.EmailTypeEnum;
import br.com.customer.service.EmailService;
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
@WebMvcTest({EmailController.class})
@ContextConfiguration(classes = {Application.class, MapperConfig.class})
public class EmailControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private EmailService emailServiceMock;

  @Test
  public void createEmail_shouldCreate() throws Exception {

    final var emailId = 1L;
    final var customerId = 1L;
    final var email = getEmail(emailId);
    final var requestJson = getJsonFromObject(getEmailRequest());

    when(emailServiceMock.create(any(Email.class))).thenReturn(email);

    mockMvc
        .perform(
            post("/v1/customers/{customerId}/emails", customerId)
                .contentType(APPLICATION_JSON)
                .content(requestJson))
        .andExpect(status().is(201))
        .andExpect(jsonPath("id").value(emailId));

    verify(emailServiceMock, times(1)).create(any(Email.class));
  }

  @Test
  public void updateEmail_shouldUpdate() throws Exception {

    final var emailId = 1L;
    final var customerId = 1L;
    final var requestJson = getJsonFromObject(getEmailRequest());

    mockMvc
        .perform(
            put("/v1/customers/{customerId}/emails/{id}", customerId, emailId)
                .contentType(APPLICATION_JSON)
                .content(requestJson))
        .andExpect(status().is(200));

    verify(emailServiceMock, times(1)).update(any(Email.class));
  }

  @Test
  public void findEmailById_shouldFind() throws Exception {

    final var emailId = 1L;
    final var customerId = 1L;
    final var type = EmailTypeEnum.WORK;
    final var email = getRandomEmail();
    final var entity = getEmail(emailId, type, email);

    when(emailServiceMock.findByCustomerIdAndId(customerId, emailId)).thenReturn(entity);

    mockMvc
        .perform(
            get("/v1/customers/{customerId}/emails/{id}", customerId, emailId)
                .contentType(APPLICATION_JSON))
        .andExpect(status().is(200))
        .andExpect(jsonPath("id").value(emailId))
        .andExpect(jsonPath("type").value(EmailTypeEnum.WORK.name()))
        .andExpect(jsonPath("email").value(email));

    verify(emailServiceMock, times(1)).findByCustomerIdAndId(customerId, emailId);
  }

  @Test
  public void findEmailById_shouldThrowNotFoundWhenNotFound() throws Exception {
    final var emailId = getRandomLong();
    final var customerId = 1L;
    when(emailServiceMock.findByCustomerIdAndId(customerId, emailId))
        .thenThrow(
            new DataNotFoundException(
                "Email with id %s and customer id %s was not found"
                    .formatted(customerId, emailId)));

    mockMvc
        .perform(
            get("/v1/customers/{customerId}/emails/{id}", customerId, emailId)
                .contentType(APPLICATION_JSON))
        .andExpect(status().is(404))
        .andExpect(
            jsonPath("description")
                .value(
                    "Email with id %s and customer id %s was not found"
                        .formatted(customerId, emailId)));

    verify(emailServiceMock, times(1)).findByCustomerIdAndId(customerId, emailId);
  }

  @Test
  public void deleteEmail_shouldDeleteEmail() throws Exception {
    final var emailId = getRandomLong();
    final var customerId = getRandomLong();

    doNothing().when(emailServiceMock).delete(any(Email.class));

    mockMvc
        .perform(
            delete("/v1/customers/{customerId}/emails/{id}", customerId, emailId)
                .contentType(APPLICATION_JSON))
        .andExpect(status().is(204));

    verify(emailServiceMock, times(1)).delete(any(Email.class));
  }

  @Test
  public void deleteEmail_shouldThrowNotFoundWhenNotFound() throws Exception {
    final var emailId = getRandomLong();
    final var customerId = getRandomLong();

    doThrow(
            new DataNotFoundException(
                "Email with id %s and customer id %s was not found".formatted(customerId, emailId)))
        .when(emailServiceMock)
        .delete(any(Email.class));

    mockMvc
        .perform(
            delete("/v1/customers/{customerId}/emails/{id}", customerId, emailId)
                .contentType(APPLICATION_JSON))
        .andExpect(status().is(404))
        .andExpect(
            jsonPath("description")
                .value(
                    "Email with id %s and customer id %s was not found"
                        .formatted(customerId, emailId)));

    verify(emailServiceMock, times(1)).delete(any(Email.class));
  }

  @Test
  public void findEmails_shouldFind() throws Exception {
    final var customerId = getRandomLong();
    var filter = new EmailFilter();
    when(emailServiceMock.findAll(customerId, filter)).thenReturn(getPageEmail());

    mockMvc
        .perform(get("/v1/customers/{customerId}/emails", customerId).contentType(APPLICATION_JSON))
        .andExpect(status().is(200));

    verify(emailServiceMock, times(1)).findAll(customerId, filter);
  }
}
