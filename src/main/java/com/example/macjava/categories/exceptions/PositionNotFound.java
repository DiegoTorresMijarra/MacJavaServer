package com.example.macjava.categories.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class PositionNotFound extends PositionException {
    public PositionNotFound(Long id) {
        super("No se puede encontrar la Posición con el id" + id);
    }
}
