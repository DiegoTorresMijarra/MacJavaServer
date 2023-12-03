package com.example.macjava.rest.workers.exceptions;

/**
 * Excepción para cuando el trabajador envía una petición incorrecta
 */
public class WorkersBadRequest extends WorkersException {
    public WorkersBadRequest(String message) {
        super(message);
    }
}
