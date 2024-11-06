package com.turnkey.turnquest.gis.quotation.exception.error;

public class PanelException extends RuntimeException{

    public PanelException(String message) {
        super(message);
    }

    public PanelException(String message, Throwable cause) {
        super(message, cause);
    }
}
