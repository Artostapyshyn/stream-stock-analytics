package com.artostapyshyn.data.analysis.service;

import com.artostapyshyn.data.analysis.model.MetaData;
import com.artostapyshyn.data.analysis.model.StockData;
import com.artostapyshyn.data.analysis.service.impl.ReportServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ReportServiceImplTest {

    private IndicatorCalculationService indicatorService;
    private ReportServiceImpl reportService;
    private StockData stockData;

    @BeforeEach
    void setUp() {
        indicatorService = mock(IndicatorCalculationService.class);
        reportService = new ReportServiceImpl(indicatorService);

        stockData = new StockData();
        MetaData metaData = new MetaData("UTC", "2023-10-01", "AAPL", "Apple Inc.", "AAPL");
        stockData.setMetaData(metaData);
    }

    @Test
    void generatePdfReport_ShouldReturnNonNullResource() {
        when(indicatorService.calculateAveragePrice(stockData)).thenReturn(Map.of("avg", BigDecimal.ONE));

        Resource resource = reportService.generatePdfReport(stockData, List.of("averagePrice"));
        assertNotNull(resource);
        assertEquals("report.pdf", resource.getFilename());
    }

    @Test
    void generateXlsxReport_ShouldReturnNonNullResource() {
        when(indicatorService.calculateAveragePrice(stockData)).thenReturn(Map.of("avg", BigDecimal.ONE));

        Resource resource = reportService.generateXlsxReport(stockData, List.of("averagePrice"));
        assertNotNull(resource);
        assertEquals("report.xlsx", resource.getFilename());
    }

    @Test
    void generateReport_WithInvalidFormat_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () ->
                reportService.generateReport(stockData, "txt", List.of("averagePrice")));
    }

    @Test
    void calculateIndicators_ShouldReturnAllIndicators() {
        when(indicatorService.calculateAveragePrice(stockData)).thenReturn(Map.of("avg", BigDecimal.ONE));
        when(indicatorService.calculatePriceChange(stockData)).thenReturn(Map.of("change", BigDecimal.TEN));
        when(indicatorService.calculatePercentagePriceChange(stockData)).thenReturn(Map.of("percent", BigDecimal.valueOf(5)));
        when(indicatorService.calculateAverageVolume(stockData)).thenReturn(Map.of("volume", BigDecimal.valueOf(1000)));
        when(indicatorService.calculateMinPrice(stockData)).thenReturn(Map.of("min", BigDecimal.ONE));
        when(indicatorService.calculateMaxPrice(stockData)).thenReturn(Map.of("max", BigDecimal.TEN));

        Map<String, Map<String, BigDecimal>> result = reportService.calculateIndicators(
                stockData,
                List.of("averagePrice", "priceChange", "percentagePriceChange", "averageVolume", "minPrice", "maxPrice")
        );

        assertEquals(6, result.size());
        assertTrue(result.containsKey("averagePrice"));
        assertTrue(result.containsKey("priceChange"));
        assertTrue(result.containsKey("percentagePriceChange"));
        assertTrue(result.containsKey("averageVolume"));
        assertTrue(result.containsKey("minPrice"));
        assertTrue(result.containsKey("maxPrice"));
    }

    @Test
    void calculateIndicators_WithInvalidIndicator_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () ->
                reportService.calculateIndicators(stockData, List.of("invalidIndicator")));
    }

    @Test
    void getFileDownloadHeaders_ShouldReturnCorrectHeaders() {
        HttpHeaders headers = reportService.getFileDownloadHeaders("report.pdf");

        assertEquals("form-data; name=\"attachment\"; filename=\"report.pdf\"", headers.getFirst(HttpHeaders.CONTENT_DISPOSITION));
        assertEquals("application/octet-stream", headers.getFirst(HttpHeaders.CONTENT_TYPE));
    }
}
