package com.artostapyshyn.data.analysis.controller;

import com.artostapyshyn.data.analysis.model.StockData;
import com.artostapyshyn.data.analysis.service.IndicatorCalculationService;
import com.artostapyshyn.data.analysis.service.StockDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AnalysisControllerTest {

    private IndicatorCalculationService indicatorService;
    private StockDataService stockDataService;
    private AnalysisController controller;
    private StockData mockStockData;
    private final String requestId = "req-123";
    private final Map<String, BigDecimal> mockResult = Map.of("key", BigDecimal.ONE);

    @BeforeEach
    void setUp() {
        indicatorService = mock(IndicatorCalculationService.class);
        stockDataService = mock(StockDataService.class);
        controller = new AnalysisController(indicatorService, stockDataService);
        mockStockData = new StockData();

        when(stockDataService.getStockDataFromQueue(requestId)).thenReturn(mockStockData);
    }

    @Test
    void calculateAveragePrice_ShouldReturnResult() {
        when(indicatorService.calculateAveragePrice(mockStockData)).thenReturn(mockResult);
        ResponseEntity<Object> response = controller.calculateAveragePrice(requestId);
        assertEquals(mockResult, response.getBody());
    }

    @Test
    void calculatePriceChange_ShouldReturnResult() {
        when(indicatorService.calculatePriceChange(mockStockData)).thenReturn(mockResult);
        ResponseEntity<Object> response = controller.calculatePriceChange(requestId);
        assertEquals(mockResult, response.getBody());
    }

    @Test
    void calculatePercentagePriceChange_ShouldReturnResult() {
        when(indicatorService.calculatePercentagePriceChange(mockStockData)).thenReturn(mockResult);
        ResponseEntity<Object> response = controller.calculatePercentagePriceChange(requestId);
        assertEquals(mockResult, response.getBody());
    }

    @Test
    void calculateAverageVolume_ShouldReturnResult() {
        when(indicatorService.calculateAverageVolume(mockStockData)).thenReturn(mockResult);
        ResponseEntity<Object> response = controller.calculateAverageVolume(requestId);
        assertEquals(mockResult, response.getBody());
    }

    @Test
    void calculateMinPrice_ShouldReturnResult() {
        when(indicatorService.calculateMinPrice(mockStockData)).thenReturn(mockResult);
        ResponseEntity<Object> response = controller.calculateMinPrice(requestId);
        assertEquals(mockResult, response.getBody());
    }

    @Test
    void calculateMaxPrice_ShouldReturnResult() {
        when(indicatorService.calculateMaxPrice(mockStockData)).thenReturn(mockResult);
        ResponseEntity<Object> response = controller.calculateMaxPrice(requestId);
        assertEquals(mockResult, response.getBody());
    }
}
