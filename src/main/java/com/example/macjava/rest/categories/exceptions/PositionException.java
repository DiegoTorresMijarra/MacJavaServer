package com.example.macjava.rest.categories.exceptions;

public abstract class PositionException extends RuntimeException {
    public PositionException(String message) {
        super(message);
    }
}
