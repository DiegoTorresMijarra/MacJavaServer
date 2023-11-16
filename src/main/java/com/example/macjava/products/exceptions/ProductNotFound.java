package com.example.macjava.products.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProductNotFound extends ProductException{
    public ProductNotFound(Long id) {
        super("Producto con id" + id + "no encontrado");
    }
}
