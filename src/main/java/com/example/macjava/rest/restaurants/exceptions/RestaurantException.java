package com.example.macjava.rest.restaurants.exceptions;
/**
 * Clase que representa una excepción de un restaurante
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
