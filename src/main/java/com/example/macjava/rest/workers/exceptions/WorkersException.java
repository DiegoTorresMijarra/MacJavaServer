package com.example.macjava.rest.workers.exceptions;

/**
 * Excepción para cuando el trabajador envía una petición incorrecta
 */
public abstract class WorkersException extends RuntimeException {
    public WorkersException(String message) {
        super(message);
    }
}
