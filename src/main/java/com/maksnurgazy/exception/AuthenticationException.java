package com.maksnurgazy.exception;


public class AuthenticationException extends RuntimeException {
	
	private static final long serialVersionUID = 5261479228157084391L;
	
	public AuthenticationException() {
		super();
	}
	
	public AuthenticationException(String message) {
		super(message);
	}
	
	public AuthenticationException(Exception exception) {
		super(exception);
	}

}
