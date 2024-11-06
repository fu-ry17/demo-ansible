package com.turnkey.turnquest.gis.quotation.exception.error;

public class QuoteCreationException extends RuntimeException{
    public QuoteCreationException(String message,Throwable t){
        super(message,t);
    }
}
