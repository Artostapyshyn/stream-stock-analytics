package com.artostapyshyn.data.analysis.controller;

import com.artostapyshyn.data.analysis.model.StockData;
import com.artostapyshyn.data.analysis.service.IndicatorCalculationService;
import com.artostapyshyn.data.analysis.service.StockDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

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

        when(stockDataService.getStockDataFromQueue(requestId)).thenReturn(Mono.just(mockStockData));
    }

    @Test
    void calculateAveragePrice_ShouldReturnResult() {
        when(indicatorService.calculateAveragePrice(mockStockData)).thenReturn(Mono.just(mockResult));
        Map<String, BigDecimal> response = controller.calculateAveragePrice(requestId).block();
        assertEquals(mockResult, response);
    }

    @Test
    void calculatePriceChange_ShouldReturnResult() {
        when(indicatorService.calculatePriceChange(mockStockData)).thenReturn(Mono.just(mockResult));
        Map<String, BigDecimal> response = controller.calculatePriceChange(requestId).block();
        assertEquals(mockResult, response);
    }

    @Test
    void calculatePercentagePriceChange_ShouldReturnResult() {
        when(indicatorService.calculatePercentagePriceChange(mockStockData)).thenReturn(Mono.just(mockResult));
        Map<String, BigDecimal> response = controller.calculatePercentagePriceChange(requestId).block();
        assertEquals(mockResult, response);
    }

    @Test
    void calculateAverageVolume_ShouldReturnResult() {
        when(indicatorService.calculateAverageVolume(mockStockData)).thenReturn(Mono.just(mockResult));
        Map<String, BigDecimal> response = controller.calculateAverageVolume(requestId).block();
        assertEquals(mockResult, response);
    }

    @Test
    void calculateMinPrice_ShouldReturnResult() {
        when(indicatorService.calculateMinPrice(mockStockData)).thenReturn(Mono.just(mockResult));
        Map<String, BigDecimal> response = controller.calculateMinPrice(requestId).block();
        assertEquals(mockResult, response);
    }

    @Test
    void calculateMaxPrice_ShouldReturnResult() {
        when(indicatorService.calculateMaxPrice(mockStockData)).thenReturn(Mono.just(mockResult));
        Map<String, BigDecimal> response = controller.calculateMaxPrice(requestId).block();
        assertEquals(mockResult, response);
    }
}
