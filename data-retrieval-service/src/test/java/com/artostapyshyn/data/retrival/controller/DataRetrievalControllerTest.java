package com.artostapyshyn.data.retrival.controller;

import com.artostapyshyn.data.retrival.service.DataRetrievalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        when(dataRetrievalService.getData("func", "symb", "5min")).thenReturn(Mono.just(mockResponse));

        Object response = controller.getData("func", "symb", "5min").block();
        assertNotNull(response);
    }

    @Test
    void testGetData() {
        String function = "func";
        String symbol = "symb";
        String interval = "5min";
        Object mockData = new Object();

        when(dataRetrievalService.getData(function, symbol, interval)).thenReturn(Mono.just(mockData));

        Mono<Object> responseMono = controller.getData(function, symbol, interval);
        Object response = responseMono.block();
        assertNotNull(response);
    }
}