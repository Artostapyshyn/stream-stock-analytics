package com.artostapyshyn.data.analysis.service;

import com.artostapyshyn.data.analysis.model.StockData;
import com.fasterxml.jackson.core.JsonProcessingException;
import reactor.core.publisher.Mono;

public interface StockDataService {
    Mono<StockData> parseJson(String json) throws JsonProcessingException;
    Mono<StockData> getStockDataFromQueue(String requestId);
}
