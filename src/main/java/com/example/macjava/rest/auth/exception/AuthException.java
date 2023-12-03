package com.example.macjava.rest.auth.exception;

/**
 * Excepción de autenticación
 */
public abstract class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }
}
