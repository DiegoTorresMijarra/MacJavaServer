package com.example.macjava.clients.storage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class storageNotFound extends storageException{
    public storageNotFound(String message) {
        super(message);
    }
}
