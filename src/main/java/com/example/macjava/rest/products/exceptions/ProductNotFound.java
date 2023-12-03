package com.example.macjava.rest.products.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción personalizada para indicar que un producto no ha sido encontrado.
 * Extiende la clase base ProductException y está anotada con @ResponseStatus(HttpStatus.NOT_FOUND)
 * para indicar el código de estado HTTP 404.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProductNotFound extends ProductException{
    /**
     * Constructor que toma un ID y construye un mensaje de error asociado a la excepción
     * @param id ID del producto no encontrado.
     */
    public ProductNotFound(Long id) {
        super("Producto con id" + id + "no encontrado");
    }
}
