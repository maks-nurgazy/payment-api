package com.maksnurgazy.exception;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class AccessDeniedException extends StatusException {
    @Serial
    private static final long serialVersionUID = 1L;

    public AccessDeniedException(String message) {
        super(HttpStatus.FORBIDDEN.value(), message);
    }
}
