package com.turnkey.turnquest.gis.quotation.exception.error;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author Titus Murithi Bundi
 */
class DocumentExceptionTest {

    @Test
    void shouldThrowDocumentExceptionWithCorrectMessageAndCause() {
        Throwable cause = new Throwable("This is the cause");
        DocumentException exception = new DocumentException("This is a message", cause);

        assertEquals("This is a message", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void shouldThrowDocumentExceptionWithNullMessageAndCause() {
        Throwable cause = null;
        DocumentException exception = new DocumentException(null, cause);

        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void shouldThrowDocumentExceptionWithEmptyMessageAndCause() {
        Throwable cause = new Throwable("");
        DocumentException exception = new DocumentException("", cause);

        assertEquals("", exception.getMessage());
        assertEquals("", exception.getCause().getMessage());
    }

}
