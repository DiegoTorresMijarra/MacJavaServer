package com.example.macjava.categories.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PositionBadRequest extends PositionException {
    public PositionBadRequest(String message) {
        super(message);
    }
}
