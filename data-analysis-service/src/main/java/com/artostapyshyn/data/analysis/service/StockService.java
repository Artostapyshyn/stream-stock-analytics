package com.artostapyshyn.data.analysis.service;

import com.artostapyshyn.data.analysis.model.StockData;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface StockService {
    StockData parseJson(String json) throws JsonProcessingException;
}
