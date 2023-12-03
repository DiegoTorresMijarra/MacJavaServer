package com.example.macjava.rest.user.exception;

/**
 * Excepción de un usuario
 * Extiende la clase RuntimeException
 */
public abstract class UserException extends RuntimeException{
    /**
     * Constructor de la excepción
     * @param message Mensaje de la excepción
     */
    public UserException(String message){
        super(message);
    }
}
