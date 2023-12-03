package com.example.macjava.rest.restaurants.exceptions;
/**
 * Excepción para cuando la petición es incorrecta
 */
public class RestaurantException extends RuntimeException{
    /**
     * Constructor de la excepción
     * @param message Mensaje de la excepción
     */
    public RestaurantException(String message){
        super(message);
    }
}
