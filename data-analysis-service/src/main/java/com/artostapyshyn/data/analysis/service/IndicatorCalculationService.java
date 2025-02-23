package com.artostapyshyn.data.analysis.service;

import com.artostapyshyn.data.analysis.model.StockData;

import java.math.BigDecimal;
import java.util.Map;

public interface IndicatorCalculationService {
    Map<String, BigDecimal> calculateAveragePrice(StockData stockData);
    Map<String, BigDecimal> calculatePriceChange(StockData stockData);
    Map<String, BigDecimal> calculatePercentagePriceChange(StockData stockData);
    Map<String, BigDecimal> calculateAverageVolume(StockData stockData);
    Map<String, BigDecimal> calculateMinPrice(StockData stockData);
    Map<String, BigDecimal> calculateMaxPrice(StockData stockData);
}
