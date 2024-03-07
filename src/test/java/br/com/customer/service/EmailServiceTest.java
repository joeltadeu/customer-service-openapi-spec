package br.com.customer.service;

import static br.com.customer.util.TestUtil.*;
import static br.com.customer.util.TestUtil.getRandomString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import br.com.customer.persistence.entity.Email;
import br.com.customer.persistence.entity.enums.EmailTypeEnum;
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
public class EmailServiceTest {
  @Mock EmailRepository emailRepositoryMock;

  @InjectMocks EmailService emailService;

  @Captor private ArgumentCaptor<Email> emailArgumentCaptor;

  @Test
  public void createEmail_shouldCreateEmail() {
    final var email = getRandomEmail();
    final var type = EmailTypeEnum.WORK;
    final var emailId = getRandomLong();

    final var incomeEmail = getEmail(null, type, email);
    final var outcomeEmail = getEmail(emailId, type, email);

    when(emailRepositoryMock.insert(emailArgumentCaptor.capture()))
        .thenReturn(outcomeEmail);

    final var result = emailService.create(incomeEmail);

    assertEquals(outcomeEmail, result);

    final var argumentCaptorValue = emailArgumentCaptor.getValue();

    assertEquals(incomeEmail.getEmail(), argumentCaptorValue.getEmail());
    assertEquals(incomeEmail.getType(), argumentCaptorValue.getType());
    assertNull(argumentCaptorValue.getId());

    verify(emailRepositoryMock, times(1)).insert(argumentCaptorValue);
  }

  @Test
  public void updateEmail_shouldUpdateEmail() {
    final var emailId = getRandomLong();
    final var customerId = getRandomLong();

    final var email = getEmail(emailId, customerId);

    when(emailRepositoryMock.findByCustomerIdAndId(anyLong(), anyLong())).thenReturn(email);

    emailService.update(email);

    verify(emailRepositoryMock, times(1)).findByCustomerIdAndId(customerId, emailId);
    verify(emailRepositoryMock, times(1)).update(email);
  }

  @Test
  public void updateEmail_shouldNotUpdateWhenEmailNotFound() {
    final var emailId = getRandomLong();
    final var customerId = getRandomLong();
    final var email = getEmail(emailId, customerId);

    when(emailRepositoryMock.findByCustomerIdAndId(anyLong(), anyLong()))
        .thenThrow(
            new DataNotFoundException(
                String.format("Email with id %s was not found", emailId)));

    final var assertThrows =
        assertThrows(DataNotFoundException.class, () -> emailService.update(email));
    assertEquals(
        "Email with id %s was not found".formatted(emailId), assertThrows.getMessage());
    verify(emailRepositoryMock, times(1)).findByCustomerIdAndId(customerId, emailId);
    verify(emailRepositoryMock, times(0)).update(email);
  }

  @Test
  public void deleteEmailById_shouldDelete() {
    final var customerId = getRandomLong();
    final var emailId = getRandomLong();
    final var email = getEmail(emailId, customerId);

    when(emailRepositoryMock.findByCustomerIdAndId(anyLong(), anyLong())).thenReturn(email);
    doNothing().when(emailRepositoryMock).delete(emailId);
    emailService.delete(email);

    verify(emailRepositoryMock, times(1)).findByCustomerIdAndId(anyLong(), anyLong());
    verify(emailRepositoryMock, times(1)).delete(anyLong());
  }

  @Test
  public void findEmailById_shouldFind() {
    final var emailId = getRandomLong();
    final var customerId = getRandomLong();
    final var returnedEmail = getEmail(emailId);

    when(emailRepositoryMock.findByCustomerIdAndId(anyLong(), anyLong()))
        .thenReturn(returnedEmail);

    final var emailReturnedFromDatabase =
        emailService.findByCustomerIdAndId(customerId, emailId);

    assertEquals(returnedEmail, emailReturnedFromDatabase);
    verify(emailRepositoryMock, times(1)).findByCustomerIdAndId(anyLong(), anyLong());
  }

  @Test
  public void findEmailById_shouldNotFind() {
    final var emailId = getRandomLong();
    final var customerId = getRandomLong();

    when(emailRepositoryMock.findByCustomerIdAndId(anyLong(), anyLong()))
        .thenThrow(
            new DataNotFoundException(
                String.format("Email with id %s was not found", emailId)));

    final var assertThrows =
        assertThrows(
            DataNotFoundException.class,
            () -> emailService.findByCustomerIdAndId(customerId, emailId));

    assertEquals(
        "Email with id %s was not found".formatted(emailId), assertThrows.getMessage());

    verify(emailRepositoryMock, times(1)).findByCustomerIdAndId(anyLong(), anyLong());
  }

  @Test
  public void findAllEmail_shouldFind() {
    final var filter = getEmailFilter();
    final var emails = getEmails();
    final var customerId = getRandomLong();

    when(emailRepositoryMock.findAll(customerId, filter)).thenReturn(emails);
    when(emailRepositoryMock.findAllCount(customerId, filter)).thenReturn(emails.size());

    final var pageEmail = emailService.findAll(customerId, filter);

    assertEquals(emails.size(), pageEmail.getTotalElements());
    assertEquals(1, pageEmail.getTotalPages());
    assertEquals(10, pageEmail.getSize());
    assertNotNull(pageEmail.getContent());
    assertEquals(0, pageEmail.getNumber());
    assertEquals(emails.size(), pageEmail.getNumberOfElements());

    verify(emailRepositoryMock, times(1)).findAll(customerId, filter);
    verify(emailRepositoryMock, times(1)).findAllCount(customerId, filter);
  }
}
