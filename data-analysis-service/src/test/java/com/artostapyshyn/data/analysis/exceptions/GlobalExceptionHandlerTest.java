package com.artostapyshyn.data.analysis.exceptions;

import com.artostapyshyn.data.analysis.exceptions.handler.ErrorResponse;
import com.artostapyshyn.data.analysis.exceptions.handler.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    void handleStockDataNotFoundException_shouldReturnNotFoundResponse() {
        String message = "Data not found in queue";
        StockDataNotFoundException ex = new StockDataNotFoundException(message);

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleStockDataNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Data not found in queue", response.getBody().getMessage());
    }

    @Test
    void handleGenericException_shouldReturnInternalServerErrorResponse() {
        String message = "Unexpected error occurred";
        Exception ex = new RuntimeException(message);

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Unexpected error occurred", response.getBody().getMessage());
    }
}

