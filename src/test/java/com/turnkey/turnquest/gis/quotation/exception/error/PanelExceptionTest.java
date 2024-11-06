package com.turnkey.turnquest.gis.quotation.exception.error;

import com.turnkey.turnquest.gis.quotation.exception.error.PanelException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PanelExceptionTest {

    @Test
    void shouldThrowPanelExceptionWithCorrectMessage() {
        PanelException exception = new PanelException("This is a message");

        assertEquals("This is a message", exception.getMessage());
    }

    @Test
    void shouldThrowPanelExceptionWithCorrectMessageAndCause() {
        Throwable cause = new Throwable("This is the cause");
        PanelException exception = new PanelException("This is a message", cause);

        assertEquals("This is a message", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void shouldThrowPanelExceptionWithNullMessageAndCause() {
        Throwable cause = null;
        PanelException exception = new PanelException(null, cause);

        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void shouldThrowPanelExceptionWithEmptyMessageAndCause() {
        Throwable cause = new Throwable("");
        PanelException exception = new PanelException("", cause);

        assertEquals("", exception.getMessage());
        assertEquals("", exception.getCause().getMessage());
    }
}