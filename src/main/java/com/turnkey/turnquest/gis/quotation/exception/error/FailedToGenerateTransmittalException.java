package com.turnkey.turnquest.gis.quotation.exception.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class FailedToGenerateTransmittalException extends RuntimeException {

    public FailedToGenerateTransmittalException(String message) {
        super(message);
    }
}
