package com.maksnurgazy.exception;

public class ExternalException extends StatusException {

	public ExternalException(String message) {
		super(401, message);
	}

	private static final long serialVersionUID = 1L;

}
