package com.artostapyshyn.data.analysis.service;

import com.artostapyshyn.data.analysis.model.StockData;

public interface IndicatorCalculationService {
    double calculateAveragePrice(StockData stockData);
    double calculatePriceChange(StockData stockData);
    double calculatePercentagePriceChange(StockData stockData);
    double calculateAverageVolume(StockData stockData);
    double calculatePercentageVolumeChange(StockData stockData);
}
