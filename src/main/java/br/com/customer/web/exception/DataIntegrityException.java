package br.com.customer.web.exception;

public class DataIntegrityException extends RuntimeException {

	private static final long serialVersionUID = 4384920945038295451L;

	public DataIntegrityException(String message) {
		super(message);
	}

	public DataIntegrityException(String message, Throwable cause) {
		super(message, cause);
	}
}
