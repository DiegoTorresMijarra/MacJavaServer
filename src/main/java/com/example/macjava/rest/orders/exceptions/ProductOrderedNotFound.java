package com.example.macjava.rest.orders.exceptions;

public class ProductOrderedNotFound extends OrderException{
    public ProductOrderedNotFound(Long id) {
        super("El producto con id "+id+" no existe.");
    }
}
