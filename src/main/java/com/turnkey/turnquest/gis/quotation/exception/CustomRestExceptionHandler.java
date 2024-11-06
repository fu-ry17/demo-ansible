package com.turnkey.turnquest.gis.quotation.exception;


import com.turnkey.turnquest.gis.quotation.aki.error.AKIValidationException;
import com.turnkey.turnquest.gis.quotation.exception.enums.ErrorType;
import com.turnkey.turnquest.gis.quotation.exception.error.DealException;
import com.turnkey.turnquest.gis.quotation.exception.error.QuoteCreationException;
import com.turnkey.turnquest.gis.quotation.exception.error.ResourceNotFoundException;
import com.turnkey.turnquest.gis.quotation.exception.payload.ApiError;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


import java.util.Collections;


@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException resourceNotFoundException) {

        ApiError apiError = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ErrorType.ERROR,
                "Q001",
                resourceNotFoundException.getMessage(),
                Collections.singletonList(resourceNotFoundException.getMessage()),
                resourceNotFoundException.toString()
        );

        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(DealException.class)
    public ResponseEntity<Object> handleDealException(DealException dealException){
        ApiError apiError = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ErrorType.ERROR,
                "Q002",
                dealException.getMessage(),
                Collections.singletonList(dealException.getMessage()),
                dealException.toString()
        );

        return new ResponseEntity<>(apiError,apiError.getStatus());
    }

    @ExceptionHandler(AKIValidationException.class)
    public ResponseEntity<Object> handleAKIValidationException(AKIValidationException akiValidationException){
        ApiError apiError = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ErrorType.ERROR,
                "Q003",
                akiValidationException.getMessage(),
                Collections.singletonList(akiValidationException.getMessage()),
                akiValidationException.toString()
        );

        return new ResponseEntity<>(apiError,apiError.getStatus());
    }

    @ExceptionHandler(QuoteCreationException.class)
    public ResponseEntity<Object> handleQuoteCreationException(QuoteCreationException quoteCreationException) {

        ApiError apiError = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ErrorType.ERROR,
                "Q004",
                quoteCreationException.getMessage(),
                Collections.singletonList(quoteCreationException.getMessage()),
                quoteCreationException.toString()
        );

        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

}
