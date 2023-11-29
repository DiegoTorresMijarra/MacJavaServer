package com.example.macjava.rest.user.exception;

public abstract class UserException extends RuntimeException{
    public UserException(String message){
        super(message);
    }
}
