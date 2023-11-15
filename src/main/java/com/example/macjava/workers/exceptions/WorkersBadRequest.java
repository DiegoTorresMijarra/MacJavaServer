package com.example.macjava.workers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class WorkersBadRequest extends WorkersException {
    public WorkersBadRequest(String message) {
        super(message);
    }
}
