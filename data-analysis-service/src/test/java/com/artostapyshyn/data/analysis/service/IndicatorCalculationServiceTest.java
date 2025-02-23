package com.artostapyshyn.data.analysis.service;

import com.artostapyshyn.data.analysis.model.StockData;
import com.artostapyshyn.data.analysis.service.impl.IndicatorCalculationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

import static com.artostapyshyn.data.analysis.util.DataUtil.generateFixedStockData;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IndicatorCalculationServiceTest {

    private IndicatorCalculationService indicatorService;

    @BeforeEach
    void setUp() {
        indicatorService = new IndicatorCalculationServiceImpl();
    }

    @Test
    void testCalculateAveragePrice() {
        StockData stockData = generateFixedStockData();
        Map<String, BigDecimal> averagePrices = indicatorService.calculateAveragePrice(stockData);
        assertEquals(90.0, averagePrices.get("2022-01-01"));
    }

    @Test
    void testCalculateAverageVolume() {
        StockData stockData = generateFixedStockData();
        Map<String, BigDecimal> averageVolumes = indicatorService.calculateAverageVolume(stockData);
        assertEquals(750.0, averageVolumes.get("2022-01-01"), String.valueOf(0.001));
    }

    @Test
    void testCalculatePercentagePriceChange() {
        StockData stockData = generateFixedStockData();
        Map<String, BigDecimal> percentagePriceChanges = indicatorService.calculatePercentagePriceChange(stockData);
        assertEquals(100.0, percentagePriceChanges.get("2022-01-01"));
    }

    @Test
    void testCalculatePriceChange() {
        StockData stockData = generateFixedStockData();
        Map<String, BigDecimal> priceChanges = indicatorService.calculatePriceChange(stockData);
        assertEquals(50.0, priceChanges.get("2022-01-01"));
    }

    @Test
    void testCalculateMinPrice() {
        StockData stockData = generateFixedStockData();
        Map<String, BigDecimal> minPrices = indicatorService.calculateMinPrice(stockData);
        assertEquals(50.0, minPrices.get("2022-01-01"));
    }

    @Test
    void testCalculateMaxPrice() {
        StockData stockData = generateFixedStockData();
        Map<String, BigDecimal> maxPrices = indicatorService.calculateMaxPrice(stockData);
        assertEquals(100.0, maxPrices.get("2022-01-01"));
    }
}
