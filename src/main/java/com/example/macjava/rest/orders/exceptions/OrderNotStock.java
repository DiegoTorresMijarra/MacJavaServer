package com.example.macjava.rest.orders.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class OrderNotStock extends OrderException {
    public OrderNotStock(Long id) {
        super("Cantidad no válida o Producto con id " + id + " no tiene stock suficiente");
    }
}
