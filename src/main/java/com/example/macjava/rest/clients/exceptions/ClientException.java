package com.example.macjava.rest.clients.exceptions;

public class ClientException extends RuntimeException{
    public ClientException(String message) {
        super(message);
    }
}
