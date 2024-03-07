package br.com.customer.web.exception;

import java.util.List;

import br.com.customer.web.exception.model.AttributeMessage;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public class BadRequestException extends RuntimeException {
  private static final long serialVersionUID = 136827568356021732L;

  private HttpStatus status;

  private final List<AttributeMessage> messages;

  public BadRequestException(List<AttributeMessage> messages) {
    this.status = HttpStatus.BAD_REQUEST;
    this.messages = messages;
  }
}
