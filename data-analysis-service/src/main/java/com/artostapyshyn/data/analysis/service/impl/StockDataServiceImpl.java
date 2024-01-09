package com.artostapyshyn.data.analysis.service.impl;

import com.artostapyshyn.data.analysis.model.StockData;
import com.artostapyshyn.data.analysis.service.StockService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StockDataServiceImpl implements StockService {

    private final ObjectMapper objectMapper;

    @Override
    public StockData parseJson(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, StockData.class);
    }
}
