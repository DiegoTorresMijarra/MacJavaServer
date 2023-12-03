package com.example.macjava.rest.products.exceptions;

/**
 * Excepción para cuando la petición es incorrecta
 */
public class ProductBadRequest extends ProductException{
    /**
     * Constructor de la excepción
     * @param message Mensaje de la excepción
     */
    public ProductBadRequest(String message){
        super(message);
    }
}
