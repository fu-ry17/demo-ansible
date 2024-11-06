package com.turnkey.turnquest.gis.quotation.exception.error;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Titus Murithi Bundi
 */
class StringSerializationExceptionTest {

    @Test
    void shouldThrowStringSerializationExceptionWithCorrectMessage() {
        StringSerializationException exception = new StringSerializationException("Serialization failed");

        assertEquals("Serialization failed", exception.getMessage());
    }

    @Test
    void shouldThrowStringSerializationExceptionWithNullMessage() {
        StringSerializationException exception = new StringSerializationException(null);

        assertNull(exception.getMessage());
    }

    @Test
    void shouldThrowStringSerializationExceptionWithEmptyMessage() {
        StringSerializationException exception = new StringSerializationException("");

        assertEquals("", exception.getMessage());
    }

}
