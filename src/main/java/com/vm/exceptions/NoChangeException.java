package com.vm.exceptions;

public class NoChangeException extends Exception{

    private String message;

    public NoChangeException(String message){
        this.message = message;
    }
    public String getMessage(){
        return message;
    }

}
