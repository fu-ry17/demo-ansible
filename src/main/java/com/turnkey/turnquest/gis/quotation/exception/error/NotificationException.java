package com.turnkey.turnquest.gis.quotation.exception.error;

public class NotificationException extends  RuntimeException{
    public NotificationException(String message, Exception t){
        super(message,t);
    }
}
