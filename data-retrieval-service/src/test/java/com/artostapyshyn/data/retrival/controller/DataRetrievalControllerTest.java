package com.artostapyshyn.data.retrival.controller;

import com.artostapyshyn.data.retrival.service.DataRetrievalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class DataRetrievalControllerTest {

    @Mock
    private DataRetrievalService dataRetrievalService;

    @InjectMocks
    private DataRetrievalController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getData_shouldReturnResponseEntity() {
        ResponseEntity<Object> mockResponse = ResponseEntity.ok("Test Data");
        when(dataRetrievalService.getData("func", "symb", "5min")).thenReturn(mockResponse);

        ResponseEntity<Object> response = controller.getData("func", "symb", "5min");

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Test Data", response.getBody());
    }
}