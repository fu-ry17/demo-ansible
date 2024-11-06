package com.turnkey.turnquest.gis.quotation.exception.error;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Titus Murithi Bundi
 */
class FailedToGenerateTransmittalExceptionTest {

    @Test
    void shouldThrowFailedToGenerateTransmittalExceptionWithCorrectMessage() {
        FailedToGenerateTransmittalException exception = new FailedToGenerateTransmittalException("Failed to generate transmittal");

        assertEquals("Failed to generate transmittal", exception.getMessage());
    }

    @Test
    void shouldThrowFailedToGenerateTransmittalExceptionWithNullMessage() {
        FailedToGenerateTransmittalException exception = new FailedToGenerateTransmittalException(null);

        assertNull(exception.getMessage());
    }

    @Test
    void shouldThrowFailedToGenerateTransmittalExceptionWithEmptyMessage() {
        FailedToGenerateTransmittalException exception = new FailedToGenerateTransmittalException("");

        assertEquals("", exception.getMessage());
    }

}
