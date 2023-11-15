package com.example.macjava.workers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class WorkersNotFound extends WorkersException{
    public WorkersNotFound(String message) {
        super("No se puede encontrar el Empleado con el " + message);
    }
}
