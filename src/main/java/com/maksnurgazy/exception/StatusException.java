package com.maksnurgazy.exception;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Setter
@Getter
public class StatusException extends RuntimeException {
	
	@Serial
	private static final long serialVersionUID = 1L;
	
	protected final Integer status;
	protected final String message;
	
	public StatusException(Integer status, String message) {
		this.status = status;
		this.message = message;
	}

}
