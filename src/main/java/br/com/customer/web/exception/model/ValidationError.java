package br.com.customer.web.exception.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError {

	private static final long serialVersionUID = 7516988994222507687L;

	private List<FieldMessage> errors = new ArrayList<>();

	public ValidationError(Integer status, String msg, LocalDateTime timestamp) {
		super(status, msg, timestamp);
	}

	public List<FieldMessage> getErrors() {
		return errors;
	}

	public void addError(String fieldName, String message) {
		errors.add(new FieldMessage(fieldName, message));
	}

}
