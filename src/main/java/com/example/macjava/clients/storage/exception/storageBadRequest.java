package com.example.macjava.clients.storage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class storageBadRequest extends storageException{
    public storageBadRequest(String message) {
        super(message);
    }
}
