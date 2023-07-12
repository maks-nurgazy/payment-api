package com.maksnurgazy.exception;

import java.io.Serial;

public class BadRequestException extends StatusException {

	public BadRequestException(String message) {
		super(401, message);
	}

	@Serial
	private static final long serialVersionUID = 1L;

}
