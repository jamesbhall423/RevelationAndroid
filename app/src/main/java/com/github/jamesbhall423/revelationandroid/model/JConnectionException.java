package com.github.jamesbhall423.revelationandroid.model;


public class JConnectionException extends Exception {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Method JConnectionException
	 *
	 *
	 */
	public JConnectionException() {
		super();
	}

	/**
	 * Method JConnectionException
	 *
	 *
	 * @param message
	 *
	 */
	public JConnectionException(String message) {
		super(message);
	}

	/**
	 * Method JConnectionException
	 *
	 *
	 * @param message
	 * @param cause
	 *
	 */
	public JConnectionException(String message, Throwable cause) {
		super(message,cause);
	}

	/**
	 * Method JConnectionException
	 *
	 *
	 * @param cause
	 *
	 */
	public JConnectionException(Throwable cause) {
		super(cause);
	}	
}
