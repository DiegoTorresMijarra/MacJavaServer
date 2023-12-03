package com.example.macjava.rest.restaurants.exceptions;

/**
 * Excepción para cuando la petición es incorrecta
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
