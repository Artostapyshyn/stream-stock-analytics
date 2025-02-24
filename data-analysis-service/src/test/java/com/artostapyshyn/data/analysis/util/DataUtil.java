package com.artostapyshyn.data.analysis.util;

import com.artostapyshyn.data.analysis.model.DailyData;
import com.artostapyshyn.data.analysis.model.MetaData;
import com.artostapyshyn.data.analysis.model.StockData;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class DataUtil {

    public static StockData generateFixedStockData() {
        MetaData metaData = generateFixedMetaData();
        Map<String, DailyData> dailyDataMap = generateFixedDailyDataMap();
        String requestId = "1957";

        return new StockData(metaData, dailyDataMap, requestId);
    }

    private static MetaData generateFixedMetaData() {
        return new MetaData(
                "Fixed Information",
                "IBM",
                "2024-01-01",
                "compact",
                "UTC"
        );
    }

    private static Map<String, DailyData> generateFixedDailyDataMap() {
        Map<String, DailyData> dailyDataMap = new HashMap<>();

        for (int i = 0; i < 5; i++) {
            DailyData dailyData = new DailyData(
                    new BigDecimal("50.0"),
                    new BigDecimal("150.0"),
                    new BigDecimal("30.0"),
                    new BigDecimal("100.0"),
                    new BigDecimal("750.0")
            );
            dailyDataMap.put("2022-01-0" + (i + 1), dailyData);
        }

        return dailyDataMap;
    }
}

