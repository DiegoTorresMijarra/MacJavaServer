package com.example.macjava.rest.orders.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class OrderBadPrice extends OrderException {
    public OrderBadPrice(Long id) {
        super("Producto con id " + id + " no tiene un precio válido o no coincide con su precio actual");
    }
}
