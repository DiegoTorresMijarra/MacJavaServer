package com.example.macjava.rest.orders.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción para cuando el pedido no es encontrado
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class OrderNotFound extends OrderException {
    public OrderNotFound(String id) {
        super("Order con id " + id+ " no encontrado");
    }
}
