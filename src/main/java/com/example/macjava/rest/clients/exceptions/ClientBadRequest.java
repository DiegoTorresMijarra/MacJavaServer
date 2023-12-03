package com.example.macjava.rest.clients.exceptions;

/**
 * Excepción para cuando el cliente envía una petición incorrecta
 */
public class ClientBadRequest extends ClientException {
    public ClientBadRequest(String message) {
        super(message);
    }
}
