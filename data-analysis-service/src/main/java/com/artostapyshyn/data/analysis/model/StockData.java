package com.artostapyshyn.data.analysis.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockData {
    private MetaData metaData;
    private Map<String, DailyData> dailyDataMap;
    private String requestId;
}
