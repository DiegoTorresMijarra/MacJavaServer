package com.example.macjava.rest.products.exceptions;
/**
 * Excepción para cuando la petición es incorrecta
 * Extiende la clase base ProductException
 */
public class ProductException extends RuntimeException{
    /**
     * Constructor de la excepción
     * @param message Mensaje de la excepción
     */
    public ProductException(String message) {
        super(message);
    }
}
