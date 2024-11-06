package com.turnkey.turnquest.gis.quotation.exception.error;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PartialPremiumExceptionTest {

    @Test
    void shouldThrowPartialPremiumExceptionWithCorrectMessageAndCause() {
        Throwable cause = new Throwable("This is the cause");
        PartialPremiumException exception = new PartialPremiumException("This is a message", cause);

        assertEquals("This is a message", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void shouldThrowPartialPremiumExceptionWithNullMessageAndCause() {
        Throwable cause = null;
        PartialPremiumException exception = new PartialPremiumException(null, cause);

        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void shouldThrowPartialPremiumExceptionWithEmptyMessageAndCause() {
        Throwable cause = new Throwable("");
        PartialPremiumException exception = new PartialPremiumException("", cause);

        assertEquals("", exception.getMessage());
        assertEquals("", exception.getCause().getMessage());
    }
}