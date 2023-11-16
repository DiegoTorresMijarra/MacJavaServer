package com.example.macjava.clients.storage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class storageInternal extends storageException {
    public storageInternal(String message) {
        super(message);
    }
}
