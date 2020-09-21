package com.vm.exceptions;

public class NoPaymentException extends Exception{
    private String message;
    private long remaining;

    public NoPaymentException(String message, long remaining){
        this.message = message;
        this.remaining = remaining;
    }
    public String getMessage(){
        return message;
    }
    public long getRemaining(){
        return remaining;
    }
}
