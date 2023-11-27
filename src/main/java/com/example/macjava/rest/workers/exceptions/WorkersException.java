package com.example.macjava.rest.workers.exceptions;

public abstract class WorkersException extends RuntimeException {
    public WorkersException(String message) {
        super(message);
    }
}
