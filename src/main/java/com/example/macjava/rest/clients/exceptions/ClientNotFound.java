package com.example.macjava.rest.clients.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

/**
 * Excepción para cuando el cliente no se encuentra
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ClientNotFound extends ClientException{
    /**
     * Constructor de la clase
     * @param id    id del cliente no encontrado
     */
    public ClientNotFound(UUID id) {
        super("Cliente con id: " + id + " no encontrado");
    }
}
