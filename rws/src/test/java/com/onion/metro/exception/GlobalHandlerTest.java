package com.onion.metro.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleException() {
        Exception exception = new Exception("Error");

        ResponseEntity<ErrorResponse> response = handler.handleException(exception);

        assertNotNull(response);
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorResponse.status());
        assertEquals("Internal server error", errorResponse.message());
    }
}
