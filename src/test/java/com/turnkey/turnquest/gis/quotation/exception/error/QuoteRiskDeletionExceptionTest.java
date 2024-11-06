package com.turnkey.turnquest.gis.quotation.exception.error;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Titus Murithi Bundi
 */
class QuoteRiskDeletionExceptionTest {

    @Test
    void shouldThrowQuoteRiskDeletionExceptionWithCorrectMessage() {
        QuoteRiskDeletionException exception = new QuoteRiskDeletionException("Risk deletion failed");

        assertEquals("Risk deletion failed", exception.getMessage());
    }

    @Test
    void shouldThrowQuoteRiskDeletionExceptionWithNullMessage() {
        QuoteRiskDeletionException exception = new QuoteRiskDeletionException(null);

        assertNull(exception.getMessage());
    }

    @Test
    void shouldThrowQuoteRiskDeletionExceptionWithEmptyMessage() {
        QuoteRiskDeletionException exception = new QuoteRiskDeletionException("");

        assertEquals("", exception.getMessage());
    }

}
