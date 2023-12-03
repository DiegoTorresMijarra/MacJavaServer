package com.example.macjava.rest.categories.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción para cuando la petición es incorrecta
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PositionBadRequest extends PositionException {
    /**
     * Constructor de la excepción
     * @param message mensaje de error
     */
    public PositionBadRequest(String message) {
        super(message);
    }
}
