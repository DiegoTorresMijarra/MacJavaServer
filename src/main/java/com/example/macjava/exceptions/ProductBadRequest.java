package com.example.macjava.exceptions;

public class ProductBadRequest extends ProductException{
    public ProductBadRequest(String message){
        super(message);
    }
}
