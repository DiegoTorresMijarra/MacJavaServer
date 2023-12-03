package com.example.macjava.rest.clients.storage.exception;

public abstract class storageException extends RuntimeException {
    public storageException(String message) {
        super(message);
    }

}
