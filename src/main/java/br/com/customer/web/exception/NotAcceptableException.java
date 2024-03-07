package br.com.customer.web.exception;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public class NotAcceptableException extends RuntimeException {

  private static final long serialVersionUID = -4097765741273059431L;

  private final HttpStatus status;

  private String message;

  public NotAcceptableException(String message) {
    this.message = message;
    this.status = HttpStatus.NOT_ACCEPTABLE;
  }

}
