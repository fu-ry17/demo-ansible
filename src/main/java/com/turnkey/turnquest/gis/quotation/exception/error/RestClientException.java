package com.turnkey.turnquest.gis.quotation.exception.error;

public class RestClientException extends RuntimeException{
    public RestClientException(String message,Throwable t){
        super(message,t);
    }
}
