package com.turnkey.turnquest.gis.quotation.exception.payload;

import com.turnkey.turnquest.gis.quotation.exception.enums.ErrorType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@AllArgsConstructor
public class ApiError {
    private HttpStatus status;
    private ErrorType errorType;
    private String errorCode;
    private String message;
    private List<String> errors;
    private String developerMessage;
}

