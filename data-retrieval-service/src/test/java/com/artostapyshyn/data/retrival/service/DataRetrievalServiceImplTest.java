package com.artostapyshyn.data.retrival.service;

import com.artostapyshyn.data.retrival.model.RequestStatistics;
import com.artostapyshyn.data.retrival.service.impl.DataRetrievalServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DataRetrievalServiceImplTest {

    @Mock
    private RequestStatisticsService requestStatisticsService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private FinancialDataSenderService financialDataSenderService;

    @InjectMocks
    private DataRetrievalServiceImpl dataRetrievalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dataRetrievalService.apikey = "test_api_key";
    }

    @Test
    void getData_shouldFetchAndSend_whenResponseIsSuccessful() {
        String function = "TIME_SERIES_INTRADAY";
        String symbol = "AAPL";
        String interval = "5min";
        Map<String, String> dummyBody = Map.of("key", "value");

        ResponseEntity<Object> mockResponse = new ResponseEntity<>(dummyBody, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), any(), isNull(), eq(Object.class)))
                .thenReturn(mockResponse);

        ArgumentCaptor<RequestStatistics> statsCaptor = ArgumentCaptor.forClass(RequestStatistics.class);

        ResponseEntity<Object> response = dataRetrievalService.getData(function, symbol, interval);

        verify(requestStatisticsService).save(statsCaptor.capture());
        verify(financialDataSenderService).sendFinancialData(contains("requestId"), anyString());

        RequestStatistics savedStats = statsCaptor.getValue();
        assertEquals(symbol + " " + interval, savedStats.getRequestType());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
