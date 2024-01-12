package com.artostapyshyn.data.analysis.service;

import com.artostapyshyn.data.analysis.model.StockData;
import java.util.Map;

public interface IndicatorCalculationService {
    Map<String, Double> calculateAveragePrice(StockData stockData);
    Map<String, Double> calculatePriceChange(StockData stockData);
    Map<String, Double> calculatePercentagePriceChange(StockData stockData);
    Map<String, Double> calculateAverageVolume(StockData stockData);
}
