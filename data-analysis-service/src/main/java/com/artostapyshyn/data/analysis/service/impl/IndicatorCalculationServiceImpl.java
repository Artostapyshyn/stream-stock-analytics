package com.artostapyshyn.data.analysis.service.impl;

import com.artostapyshyn.data.analysis.model.DailyData;
import com.artostapyshyn.data.analysis.model.StockData;
import com.artostapyshyn.data.analysis.service.IndicatorCalculationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class IndicatorCalculationServiceImpl implements IndicatorCalculationService {

    private double calculateTotal(Map<String, DailyData> dailyDataMap, Function<DailyData, Double> calculationFunction) {
        double total = 0.0;

        for (Map.Entry<String, DailyData> entry : dailyDataMap.entrySet()) {
            DailyData dailyData = entry.getValue();
            total += calculationFunction.apply(dailyData);
        }

        return total;
    }

    private double calculateAverage(Map<String, DailyData> dailyDataMap, Function<DailyData, Double> calculationFunction) {
        int count = dailyDataMap.size();

        if (count > 0) {
            double total = calculateTotal(dailyDataMap, calculationFunction);
            return total / count;
        } else {
            return 0.0;
        }
    }

    @Override
    public double calculateAveragePrice(StockData stockData) {
        Map<String, DailyData> dailyDataMap = stockData.getDailyDataMap();
        return calculateAverage(dailyDataMap, dailyData -> (dailyData.getHigh() + dailyData.getLow()) / 2);
    }

    @Override
    public double calculatePriceChange(StockData stockData) {
        Map<String, DailyData> dailyDataMap = stockData.getDailyDataMap();
        return calculateAverage(dailyDataMap, dailyData -> dailyData.getClose() - dailyData.getOpen());
    }

    @Override
    public double calculatePercentagePriceChange(StockData stockData) {
        // TODO
        return 0;
    }

    @Override
    public double calculateAverageVolume(StockData stockData) {
        Map<String, DailyData> dailyDataMap = stockData.getDailyDataMap();
        return calculateAverage(dailyDataMap, DailyData::getVolume);
    }

    @Override
    public double calculatePercentageVolumeChange(StockData stockData) {
        // TODO
        return 0;
    }
}

