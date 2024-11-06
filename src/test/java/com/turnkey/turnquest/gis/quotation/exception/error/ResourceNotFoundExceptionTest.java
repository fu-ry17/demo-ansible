package com.turnkey.turnquest.gis.quotation.exception.error;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Titus Murithi Bundi
 */
class ResourceNotFoundExceptionTest {

    @Test
    void shouldThrowResourceNotFoundExceptionWithCorrectMessage() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Resource", "Field", "Value");

        assertEquals("Resource not found with Field : 'Value'", exception.getMessage());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWithNullFieldAndValue() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Resource", null, null);

        assertEquals("Resource not found with null : 'null'", exception.getMessage());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWithEmptyFieldAndValue() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Resource", "", "");

        assertEquals("Resource not found with  : ''", exception.getMessage());
    }

    @Test
    void shouldReturnCorrectResourceName() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Resource", "Field", "Value");

        assertEquals("Resource", exception.getResourceName());
    }

    @Test
    void shouldReturnCorrectFieldName() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Resource", "Field", "Value");

        assertEquals("Field", exception.getFieldName());
    }

    @Test
    void shouldReturnCorrectFieldValue() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Resource", "Field", "Value");

        assertEquals("Value", exception.getFieldValue());
    }

}
