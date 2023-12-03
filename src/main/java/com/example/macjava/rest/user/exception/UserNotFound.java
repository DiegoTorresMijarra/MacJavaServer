package com.example.macjava.rest.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

/**
 * Excepción personalizada para indicar que un usuario no ha sido encontrado.
 * Extiende la clase base UserException y está anotada con
 * @ResponseStatus(HttpStatus.NOT_FOUND) para indicar el código de estado HTTP 404.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFound extends UserException {
    public UserNotFound(String message) {
        super(message);
    }

    /**
     * Constructor que toma un ID y construye un mensaje de error asociado a la excepción
     * @param id  ID del usuario no encontrado
     */
    public UserNotFound(UUID id) {
        super("Usuario con id " + id + " no encontrado");
    }
}
