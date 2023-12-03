package com.example.macjava.rest.orders.exceptions;
/**
 * Excepción para cuando el producto pedido no es encontrado
 */
public class ProductOrderedNotFound extends OrderException{
    public ProductOrderedNotFound(Long id) {
        super("El producto con id "+id+" no existe.");
    }
}
