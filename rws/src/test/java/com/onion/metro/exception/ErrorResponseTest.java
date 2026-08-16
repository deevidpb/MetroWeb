package com.onion.metro.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void testErrorResponseCreation() {
        ErrorResponse response = new ErrorResponse(500, "Test message");

        assertNotNull(response);
        assertEquals(500, response.status());
        assertEquals("Test message", response.message());
    }

    @Test
    void testErrorResponseWithNullMessage() {
        ErrorResponse response = new ErrorResponse(500, null);
        assertNotNull(response);
        assertEquals(500, response.status());
        assertNull(response.message());
    }
}
