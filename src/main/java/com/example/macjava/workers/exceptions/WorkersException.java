package com.example.macjava.workers.exceptions;

public abstract class WorkersException extends RuntimeException {
    public WorkersException(String message) {
        super(message);
    }
}
