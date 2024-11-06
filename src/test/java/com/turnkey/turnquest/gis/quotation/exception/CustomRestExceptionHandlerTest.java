package com.turnkey.turnquest.gis.quotation.exception;

import com.turnkey.turnquest.gis.quotation.aki.error.AKIValidationException;
import com.turnkey.turnquest.gis.quotation.exception.enums.ErrorType;
import com.turnkey.turnquest.gis.quotation.exception.error.DealException;
import com.turnkey.turnquest.gis.quotation.exception.error.QuoteCreationException;
import com.turnkey.turnquest.gis.quotation.exception.error.ResourceNotFoundException;
import com.turnkey.turnquest.gis.quotation.exception.payload.ApiError;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Titus Murithi Bundi
 */
class CustomRestExceptionHandlerTest {

    @Test
    void shouldHandleResourceNotFoundException() {
        CustomRestExceptionHandler handler = new CustomRestExceptionHandler();
        ResourceNotFoundException exception = new ResourceNotFoundException("Resource", "Field", "Value");

        ResponseEntity<Object> response = handler.handleResourceNotFound(exception);

        assertTrue(response.getBody() instanceof ApiError);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Q001", ((ApiError) response.getBody()).getErrorCode());
    }

    @Test
    void shouldHandleDealException() {
        CustomRestExceptionHandler handler = new CustomRestExceptionHandler();
        DealException exception = new DealException("Deal exception occurred", new Throwable("Cause"));

        ResponseEntity<Object> response = handler.handleDealException(exception);

        assertTrue(response.getBody() instanceof ApiError);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Q002", ((ApiError) response.getBody()).getErrorCode());
    }

    @Test
    void shouldHandleAKIValidationException() {
        CustomRestExceptionHandler handler = new CustomRestExceptionHandler();
        AKIValidationException exception = new AKIValidationException("AKI validation failed");

        ResponseEntity<Object> response = handler.handleAKIValidationException(exception);

        assertTrue(response.getBody() instanceof ApiError);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Q003", ((ApiError) response.getBody()).getErrorCode());
    }

    @Test
    void shouldHandleQuoteCreationException() {
        CustomRestExceptionHandler handler = new CustomRestExceptionHandler();
        QuoteCreationException exception = new QuoteCreationException("Quote creation failed", new Throwable("Cause"));

        ResponseEntity<Object> response = handler.handleQuoteCreationException(exception);

        assertTrue(response.getBody() instanceof ApiError);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Q004", ((ApiError) response.getBody()).getErrorCode());
    }


    @Test
    void shouldCreateApiErrorWithGivenValues() {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ErrorType.ERROR, "400", "Bad Request", Arrays.asList("Error 1", "Error 2"), "Developer Message");

        assertEquals(HttpStatus.BAD_REQUEST, apiError.getStatus());
        assertEquals(ErrorType.ERROR, apiError.getErrorType());
        assertEquals("400", apiError.getErrorCode());
        assertEquals("Bad Request", apiError.getMessage());
        assertEquals(Arrays.asList("Error 1", "Error 2"), apiError.getErrors());
        assertEquals("Developer Message", apiError.getDeveloperMessage());
    }

    @Test
    void shouldCreateApiErrorWithNoErrors() {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ErrorType.ERROR, "400", "Bad Request", Collections.emptyList(), "Developer Message");

        assertEquals(HttpStatus.BAD_REQUEST, apiError.getStatus());
        assertEquals(ErrorType.ERROR, apiError.getErrorType());
        assertEquals("400", apiError.getErrorCode());
        assertEquals("Bad Request", apiError.getMessage());
        assertEquals(Collections.emptyList(), apiError.getErrors());
        assertEquals("Developer Message", apiError.getDeveloperMessage());
    }

}
