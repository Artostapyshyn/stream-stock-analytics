package com.artostapyshyn.data.analysis.service;

import com.artostapyshyn.data.analysis.model.StockData;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Map;

public interface IndicatorCalculationService {
    Mono<Map<String, BigDecimal>> calculateAveragePrice(StockData stockData);
    Mono<Map<String, BigDecimal>> calculatePriceChange(StockData stockData);
    Mono<Map<String, BigDecimal>> calculatePercentagePriceChange(StockData stockData);
    Mono<Map<String, BigDecimal>> calculateAverageVolume(StockData stockData);
    Mono<Map<String, BigDecimal>> calculateMinPrice(StockData stockData);
    Mono<Map<String, BigDecimal>> calculateMaxPrice(StockData stockData);
}
