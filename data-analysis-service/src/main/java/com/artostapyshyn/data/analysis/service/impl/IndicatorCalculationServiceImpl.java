package com.artostapyshyn.data.analysis.service.impl;

import com.artostapyshyn.data.analysis.model.DailyData;
import com.artostapyshyn.data.analysis.model.StockData;
import com.artostapyshyn.data.analysis.service.IndicatorCalculationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class IndicatorCalculationServiceImpl implements IndicatorCalculationService {

    private Map<String, BigDecimal> calculateAverage(Map<String, DailyData> dailyDataMap, Function<DailyData, BigDecimal> calculationFunction) {
        Map<String, BigDecimal> resultMap = new HashMap<>();
        for (Map.Entry<String, DailyData> entry : dailyDataMap.entrySet()) {
            DailyData dailyData = entry.getValue();
            BigDecimal result = calculationFunction.apply(dailyData);
            resultMap.put(entry.getKey(), result);
        }

        return resultMap;
    }

    @Override
    public Map<String, BigDecimal> calculateAveragePrice(StockData stockData) {
        Map<String, DailyData> dailyDataMap = stockData.getDailyDataMap();
        return calculateAverage(dailyDataMap, dailyData -> (dailyData.getHigh().add(dailyData.getLow())).divide(BigDecimal.valueOf(2)));
    }

    @Override
    public Map<String, BigDecimal> calculatePriceChange(StockData stockData) {
        Map<String, DailyData> dailyDataMap = stockData.getDailyDataMap();
        return calculateAverage(dailyDataMap, dailyData -> dailyData.getClose().subtract(dailyData.getOpen()));
    }

    @Override
    public Map<String, BigDecimal> calculatePercentagePriceChange(StockData stockData) {
        Map<String, DailyData> dailyDataMap = stockData.getDailyDataMap();

        if (dailyDataMap.isEmpty()) {
            return new HashMap<>();
        }

        Map<String, BigDecimal> resultMap = new HashMap<>();
        for (Map.Entry<String, DailyData> entry : dailyDataMap.entrySet()) {
            DailyData dailyData = entry.getValue();
            BigDecimal open = dailyData.getOpen();
            BigDecimal close = dailyData.getClose();
            BigDecimal percentageChange = ((close.subtract(open)).divide(open)).multiply(BigDecimal.valueOf(100));
            resultMap.put(entry.getKey(), percentageChange);
        }

        return resultMap;
    }

    @Override
    public Map<String, BigDecimal> calculateAverageVolume(StockData stockData) {
        Map<String, DailyData> dailyDataMap = stockData.getDailyDataMap();
        return calculateAverage(dailyDataMap, DailyData::getVolume);
    }

    @Override
    public Map<String, BigDecimal> calculateMinPrice(StockData stockData) {
        Map<String, DailyData> dailyDataMap = stockData.getDailyDataMap();

        if (dailyDataMap.isEmpty()) {
            return new HashMap<>();
        }

        Map<String, BigDecimal> resultMap = new HashMap<>();
        for (Map.Entry<String, DailyData> entry : dailyDataMap.entrySet()) {
            DailyData dailyData = entry.getValue();
            BigDecimal minPrice = dailyData.getOpen().min(dailyData.getClose());
            resultMap.put(entry.getKey(), minPrice);
        }

        return resultMap;
    }

    @Override
    public Map<String, BigDecimal> calculateMaxPrice(StockData stockData) {
        Map<String, DailyData> dailyDataMap = stockData.getDailyDataMap();

        if (dailyDataMap.isEmpty()) {
            return new HashMap<>();
        }

        Map<String, BigDecimal> resultMap = new HashMap<>();
        for (Map.Entry<String, DailyData> entry : dailyDataMap.entrySet()) {
            DailyData dailyData = entry.getValue();
            BigDecimal maxPrice = dailyData.getOpen().max(dailyData.getClose());
            resultMap.put(entry.getKey(), maxPrice);
        }

        return resultMap;
    }
}