package com.example.macjava.rest.categories.exceptions;

/**
 * Excepción para cuando la petición es incorrecta
 */
public abstract class PositionException extends RuntimeException {
    public PositionException(String message) {
        super(message);
    }
}
