package com.example.macjava.rest.orders.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción para cuando el precio de un producto no es válido
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class OrderBadPrice extends OrderException {
    /**
     * Constructor
     * @param id id del producto
     */
    public OrderBadPrice(Long id) {
        super("Producto con id " + id + " no tiene un precio válido o no coincide con su precio actual");
    }
}
