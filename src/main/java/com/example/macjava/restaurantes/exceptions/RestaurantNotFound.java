package com.example.macjava.restaurantes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RestaurantNotFound extends RestaurantException{
    public RestaurantNotFound(Long id){
        super("Cliente con id "+ id +" no encontrado.");
    }
}
