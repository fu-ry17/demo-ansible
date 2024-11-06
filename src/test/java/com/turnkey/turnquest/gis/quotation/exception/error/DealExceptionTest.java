package com.turnkey.turnquest.gis.quotation.exception.error;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Titus Murithi Bundi
 */
class DealExceptionTest {

    @Test
    void shouldThrowDealExceptionWithCorrectMessageAndCause() {
        Throwable cause = new Throwable("This is the cause");
        DealException exception = new DealException("This is a message", cause);

        assertEquals("This is a message", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void shouldThrowDealExceptionWithNullMessageAndCause() {
        Throwable cause = null;
        DealException exception = new DealException(null, cause);

        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void shouldThrowDealExceptionWithEmptyMessageAndCause() {
        Throwable cause = new Throwable("");
        DealException exception = new DealException("", cause);

        assertEquals("", exception.getMessage());
        assertEquals("", exception.getCause().getMessage());
    }

}
