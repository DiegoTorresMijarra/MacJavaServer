package com.example.macjava.rest.restaurants.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
/**
 * Excepción personalizada para indicar que un restaurante no ha sido encontrado.
 * Extiende la clase base RestaurantException y está anotada con
 * @ResponseStatus(HttpStatus.NOT_FOUND) para indicar el código de estado HTTP 404.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class RestaurantNotFound extends RestaurantException{
    /**
     * Constructor que toma un ID y construye un mensaje de error asociado a la excepción
     * @param id ID del restaurante no encontrado.
     */
    public RestaurantNotFound(Long id){
        super("Cliente con id "+ id +" no encontrado.");
    }
}
