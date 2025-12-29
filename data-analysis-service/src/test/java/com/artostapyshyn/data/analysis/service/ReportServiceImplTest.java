package com.artostapyshyn.data.analysis.service;

import com.artostapyshyn.data.analysis.model.MetaData;
import com.artostapyshyn.data.analysis.model.StockData;
import com.artostapyshyn.data.analysis.service.impl.ReportServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;
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
        when(indicatorService.calculateAveragePrice(stockData)).thenReturn(Mono.just(Map.of("avg", BigDecimal.ONE)));

        Mono<Resource> resourceMono = reportService.generateReport(stockData, "pdf", List.of("averagePrice"));
        Resource resource = resourceMono.block();
        assertNotNull(resource);
        assertEquals("report.pdf", resource.getFilename());
    }

    @Test
    void generateXlsxReport_ShouldReturnNonNullResource() {
        when(indicatorService.calculateAveragePrice(stockData)).thenReturn(Mono.just(Map.of("avg", BigDecimal.ONE)));

        Mono<Resource> resourceMono = reportService.generateReport(stockData, "xlsx", List.of("averagePrice"));
        Resource resource = resourceMono.block();
        assertNotNull(resource);
        assertEquals("report.xlsx", resource.getFilename());
    }

    @Test
    void calculateIndicators_ShouldReturnAllIndicators() throws Exception {
        when(indicatorService.calculateAveragePrice(stockData)).thenReturn(Mono.just(Map.of("avg", BigDecimal.ONE)));
        when(indicatorService.calculatePriceChange(stockData)).thenReturn(Mono.just(Map.of("change", BigDecimal.TEN)));
        when(indicatorService.calculatePercentagePriceChange(stockData)).thenReturn(Mono.just(Map.of("percent", BigDecimal.valueOf(5))));
        when(indicatorService.calculateAverageVolume(stockData)).thenReturn(Mono.just(Map.of("volume", BigDecimal.valueOf(1000))));
        when(indicatorService.calculateMinPrice(stockData)).thenReturn(Mono.just(Map.of("min", BigDecimal.ONE)));
        when(indicatorService.calculateMaxPrice(stockData)).thenReturn(Mono.just(Map.of("max", BigDecimal.TEN)));

        Method method = ReportServiceImpl.class.getDeclaredMethod("calculateIndicators", StockData.class, List.class);
        method.setAccessible(true);
        @SuppressWarnings("unchecked")
        Mono<Map<String, Map<String, BigDecimal>>> resultMono = (Mono<Map<String, Map<String, BigDecimal>>>) method.invoke(reportService, stockData, List.of("averagePrice", "priceChange", "percentagePriceChange", "averageVolume", "minPrice", "maxPrice"));
        Map<String, Map<String, BigDecimal>> result = resultMono.block();

        assertNotNull(result);
        assertEquals(6, result.size());
        assertTrue(result.containsKey("averagePrice"));
        assertTrue(result.containsKey("priceChange"));
        assertTrue(result.containsKey("percentagePriceChange"));
        assertTrue(result.containsKey("averageVolume"));
        assertTrue(result.containsKey("minPrice"));
        assertTrue(result.containsKey("maxPrice"));
    }

    @Test
    void getFileDownloadHeaders_ShouldReturnCorrectHeaders() {
        HttpHeaders headers = reportService.getFileDownloadHeaders("report.pdf");

        String disposition = headers.getFirst(HttpHeaders.CONTENT_DISPOSITION);
        assertNotNull(disposition);
        assertTrue(disposition.contains("attachment"));
        assertTrue(disposition.contains("report.pdf"));
        assertEquals("application/octet-stream", headers.getFirst(HttpHeaders.CONTENT_TYPE));
    }
}
