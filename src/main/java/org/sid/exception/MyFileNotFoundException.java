package org.sid.exception;

/**
 * C'est une classe d'exception personnalisée qui étend RuntimeException
 */
public class MyFileNotFoundException extends RuntimeException {

	public MyFileNotFoundException(String message) {
		super(message);
	}

	public MyFileNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
