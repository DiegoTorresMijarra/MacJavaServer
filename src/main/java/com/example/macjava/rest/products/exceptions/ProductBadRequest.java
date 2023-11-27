package com.example.macjava.rest.products.exceptions;

public class ProductBadRequest extends ProductException{
    public ProductBadRequest(String message){
        super(message);
    }
}
