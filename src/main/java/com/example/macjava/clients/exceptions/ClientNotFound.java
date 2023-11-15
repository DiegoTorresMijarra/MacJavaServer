package com.example.macjava.clients.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ClientNotFound extends ClientException{
    public ClientNotFound(UUID id) {
        super("Cliente con id: " + id + " no encontrado");
    }
}
