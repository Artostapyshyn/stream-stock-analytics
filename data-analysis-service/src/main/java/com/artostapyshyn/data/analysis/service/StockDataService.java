package com.artostapyshyn.data.analysis.service;

import com.artostapyshyn.data.analysis.model.StockData;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface StockDataService {
    StockData parseJson(String json) throws JsonProcessingException;
    StockData getStockDataFromQueue(String requestId);
}
