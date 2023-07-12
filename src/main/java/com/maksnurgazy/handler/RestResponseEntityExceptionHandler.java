package com.maksnurgazy.handler;

import com.maksnurgazy.exception.AccessDeniedException;
import com.maksnurgazy.exception.AuthenticationException;
import com.maksnurgazy.exception.BadRequestException;
import com.maksnurgazy.exception.NotFoundException;
import com.maksnurgazy.model.StatusResponse;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    private final MessageSource messageSource;

    public RestResponseEntityExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler({org.springframework.security.access.AccessDeniedException.class})
    public ResponseEntity<StatusResponse> handleAccessDenied(org.springframework.security.access.AccessDeniedException ex) {
        StatusResponse response = new StatusResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<StatusResponse> handleNotFoundException(NotFoundException ex) {
        StatusResponse response = new StatusResponse(ex.getStatus(), ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = BadRequestException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<StatusResponse> handleTokenRefreshException(BadRequestException ex, WebRequest request) {
        StatusResponse response = new StatusResponse(ex.getStatus(), ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }



    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<StatusResponse> handleAuthenticationException(AuthenticationException ex) {
        StatusResponse response = new StatusResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = UsernameNotFoundException.class)
    public ResponseEntity<StatusResponse> handleTokenRefreshException(UsernameNotFoundException ex, WebRequest request) {
        StatusResponse response = new StatusResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<StatusResponse> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        StatusResponse response = new StatusResponse(ex.getStatus(), ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }


    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<StatusResponse> handleBadCredentialException(BadCredentialsException ex, WebRequest request) {
        StatusResponse response = new StatusResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }


    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<StatusResponse> handleMethodArgumentException(MethodArgumentTypeMismatchException ex, WebRequest request) {
        StatusResponse response = new StatusResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<StatusResponse> handleGlobalException(Exception ex) {
        StatusResponse response = new StatusResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
