package com.example.macjava.rest.restaurants.exceptions;

/**
 * Clase que representa una excepción de un restaurante
 * Extiendo la clase base RestaurantException
 */
public class RestaurantBadRequest extends RestaurantException{
    /**
     * Constructor de la excepción
     * @param message Mensaje de la excepción
     */
    public RestaurantBadRequest(String message){
        super(message);
    }
}
