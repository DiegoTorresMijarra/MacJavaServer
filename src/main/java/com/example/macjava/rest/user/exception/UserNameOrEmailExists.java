package com.example.macjava.rest.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción personalizada para indicar que un usuario no ha sido encontrado.
 * Extiende la clase base UserException y está anotada con
 * @ResponseStatus(HttpStatus.NOT_FOUND) para indicar el código de estado HTTP 404.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNameOrEmailExists extends UserException {
    /**
     * Constructor que toma un ID y construye un mensaje de error asociado a la excepción
     * @param message Mensaje de error asociado a la excepción
     */
    public UserNameOrEmailExists(String message) {
        super(message);
    }
}
