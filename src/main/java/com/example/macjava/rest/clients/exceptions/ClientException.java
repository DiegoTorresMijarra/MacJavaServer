package com.example.macjava.rest.clients.exceptions;

/**
 * Excepción para cuando el cliente envía una petición incorrecta
 */
public class ClientException extends RuntimeException{
    public ClientException(String message) {
        super(message);
    }
}
