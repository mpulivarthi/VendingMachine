package com.vm.exceptions;

public class ProductNotAvailableException extends Exception{
    private String message;

    public ProductNotAvailableException(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
