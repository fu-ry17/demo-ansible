package com.turnkey.turnquest.gis.quotation.exception.error;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Titus Murithi Bundi
 */
class QuoteCreationExceptionTest {

    @Test
    void shouldThrowQuoteCreationExceptionWithCorrectMessageAndCause() {
        Throwable cause = new Throwable("This is the cause");
        QuoteCreationException exception = new QuoteCreationException("This is a message", cause);

        assertEquals("This is a message", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void shouldThrowQuoteCreationExceptionWithNullMessageAndCause() {
        Throwable cause = null;
        QuoteCreationException exception = new QuoteCreationException(null, cause);

        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void shouldThrowQuoteCreationExceptionWithEmptyMessageAndCause() {
        Throwable cause = new Throwable("");
        QuoteCreationException exception = new QuoteCreationException("", cause);

        assertEquals("", exception.getMessage());
        assertEquals("", exception.getCause().getMessage());
    }

}
