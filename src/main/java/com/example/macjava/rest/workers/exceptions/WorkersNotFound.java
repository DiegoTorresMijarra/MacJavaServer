package com.example.macjava.rest.workers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción para cuando el trabajador no se encuentra
 */
@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class WorkersNotFound extends WorkersException{
    /**
     * Constructor de la excepción
     * @param message mensaje de error
     */
    public WorkersNotFound(String message) {
        super("No se puede encontrar el Empleado con el " + message);
    }
}
