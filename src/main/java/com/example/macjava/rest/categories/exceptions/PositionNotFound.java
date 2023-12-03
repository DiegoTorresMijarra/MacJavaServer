package com.example.macjava.rest.categories.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepcion para cuando no se encuentra la posición
 */
@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class PositionNotFound extends PositionException {
    /**
     * Constructor de la excepción
     * @param id id de la posición no encontrada
     */
    public PositionNotFound(Long id) {
        super("No se puede encontrar la Posición con el id" + id);
    }
}
