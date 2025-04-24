package com.artostapyshyn.data.analysis.service.impl;

import com.artostapyshyn.data.analysis.config.MessageMapHolder;
import com.artostapyshyn.data.analysis.exceptions.StockDataNotFoundException;
import com.artostapyshyn.data.analysis.model.StockData;
import com.artostapyshyn.data.analysis.service.StockDataService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StockDataServiceImpl implements StockDataService {

    private final ObjectMapper objectMapper;
    private final MessageMapHolder messageMapHolder;

    @Override
    public StockData parseJson(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, StockData.class);
    }

    @Override
    public StockData getStockDataFromQueue(String requestId) {
        
        String json = messageMapHolder.getStockDataMap().get(requestId);

        if (json == null) {
            throw new StockDataNotFoundException("Failed to get StockData from RabbitMQ");
        }

        StockData stockData;
        try {
            stockData = parseJson(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return stockData;
    }
}
