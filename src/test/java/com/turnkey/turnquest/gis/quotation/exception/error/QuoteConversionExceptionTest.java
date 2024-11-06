package com.turnkey.turnquest.gis.quotation.exception.error;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Titus Murithi Bundi
 */
class QuoteConversionExceptionTest {

    @Test
    void shouldThrowQuoteConversionExceptionWithCorrectMessageAndCause() {
        Throwable cause = new Throwable("This is the cause");
        QuoteConversionException exception = new QuoteConversionException("This is a message", cause);

        assertEquals("This is a message", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void shouldThrowQuoteConversionExceptionWithNullMessageAndCause() {
        Throwable cause = null;
        QuoteConversionException exception = new QuoteConversionException(null, cause);

        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void shouldThrowQuoteConversionExceptionWithEmptyMessageAndCause() {
        Throwable cause = new Throwable("");
        QuoteConversionException exception = new QuoteConversionException("", cause);

        assertEquals("", exception.getMessage());
        assertEquals("", exception.getCause().getMessage());
    }

}
