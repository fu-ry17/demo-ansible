package com.turnkey.turnquest.gis.quotation.exception.error;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Titus Murithi Bundi
 */
class StringDeserializationExceptionTest {

    @Test
    void shouldThrowStringDeserializationExceptionWithCorrectMessage() {
        StringDeserializationException exception = new StringDeserializationException("Deserialization failed");

        assertEquals("Deserialization failed", exception.getMessage());
    }

    @Test
    void shouldThrowStringDeserializationExceptionWithNullMessage() {
        StringDeserializationException exception = new StringDeserializationException(null);

        assertNull(exception.getMessage());
    }

    @Test
    void shouldThrowStringDeserializationExceptionWithEmptyMessage() {
        StringDeserializationException exception = new StringDeserializationException("");

        assertEquals("", exception.getMessage());
    }

}
