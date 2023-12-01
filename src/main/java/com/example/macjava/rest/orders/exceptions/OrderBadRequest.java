package com.example.macjava.rest.orders.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class OrderBadRequest extends OrderException {
    public OrderBadRequest(String message) {
        super("Error en el pedido (Bad Request): " + message);
    }
}
