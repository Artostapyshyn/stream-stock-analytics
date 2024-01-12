package com.artostapyshyn.data.analysis.service.impl;

import com.artostapyshyn.data.analysis.model.DailyData;
import com.artostapyshyn.data.analysis.model.StockData;
import com.artostapyshyn.data.analysis.service.IndicatorCalculationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class IndicatorCalculationServiceImpl implements IndicatorCalculationService {

    private Map<String, Double> calculateAverage(Map<String, DailyData> dailyDataMap, Function<DailyData, Double> calculationFunction) {
        Map<String, Double> resultMap = new HashMap<>();

        for (Map.Entry<String, DailyData> entry : dailyDataMap.entrySet()) {
            DailyData dailyData = entry.getValue();
            double result = calculationFunction.apply(dailyData);
            resultMap.put(entry.getKey(), result);
        }

        return resultMap;
    }

    @Override
    public Map<String, Double> calculateAveragePrice(StockData stockData) {
        Map<String, DailyData> dailyDataMap = stockData.getDailyDataMap();
        return calculateAverage(dailyDataMap, dailyData -> (dailyData.getHigh() + dailyData.getLow()) / 2);
    }

    @Override
    public Map<String, Double> calculatePriceChange(StockData stockData) {
        Map<String, DailyData> dailyDataMap = stockData.getDailyDataMap();
        return calculateAverage(dailyDataMap, dailyData -> dailyData.getClose() - dailyData.getOpen());
    }

    @Override
    public Map<String, Double> calculatePercentagePriceChange(StockData stockData) {
        Map<String, DailyData> dailyDataMap = stockData.getDailyDataMap();

        if (dailyDataMap.isEmpty()) {
            return new HashMap<>();
        }

        Map<String, Double> resultMap = new HashMap<>();

        for (Map.Entry<String, DailyData> entry : dailyDataMap.entrySet()) {
            DailyData dailyData = entry.getValue();
            double open = dailyData.getOpen();
            double close = dailyData.getClose();
            double percentageChange = ((close - open) / open) * 100;
            resultMap.put(entry.getKey(), percentageChange);
        }

        return resultMap;
    }

    @Override
    public Map<String, Double> calculateAverageVolume(StockData stockData) {
        Map<String, DailyData> dailyDataMap = stockData.getDailyDataMap();
        return calculateAverage(dailyDataMap, DailyData::getVolume);
    }
}