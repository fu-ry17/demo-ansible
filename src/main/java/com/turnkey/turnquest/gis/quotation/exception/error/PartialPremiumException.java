package com.turnkey.turnquest.gis.quotation.exception.error;

public class PartialPremiumException extends RuntimeException{
    public PartialPremiumException(String message,Throwable t){
        super(message,t);
    }
}
