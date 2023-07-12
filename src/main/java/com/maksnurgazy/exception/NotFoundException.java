package com.maksnurgazy.exception;

public class NotFoundException extends StatusException {

	public NotFoundException(String message) {
		super(404, message);
	}

	private static final long serialVersionUID = 1L;

}
