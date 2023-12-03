package com.example.macjava.rest.orders.exceptions;

public abstract class OrderException extends RuntimeException {
    public OrderException(String message) {
        super(message);
    }
}
