package com.example.macjava.rest.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción de autenticación  404
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class AuthSingInInvalid extends AuthException {
    /**
     * Constructor
     * @param message mensaje de error
     */
    public AuthSingInInvalid(String message) {
        super(message);
    }
}
