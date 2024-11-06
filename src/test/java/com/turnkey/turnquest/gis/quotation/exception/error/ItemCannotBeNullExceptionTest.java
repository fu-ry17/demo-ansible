package com.turnkey.turnquest.gis.quotation.exception.error;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Titus Murithi Bundi
 */
class ItemCannotBeNullExceptionTest {

    @Test
    void shouldThrowItemCannotBeNullExceptionWithCorrectMessage() {
        ItemCannotBeNullException exception = new ItemCannotBeNullException("Item cannot be null");

        assertEquals("Item cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowItemCannotBeNullExceptionWithNullMessage() {
        ItemCannotBeNullException exception = new ItemCannotBeNullException(null);

        assertNull(exception.getMessage());
    }

    @Test
    void shouldThrowItemCannotBeNullExceptionWithEmptyMessage() {
        ItemCannotBeNullException exception = new ItemCannotBeNullException("");

        assertEquals("", exception.getMessage());
    }

}
