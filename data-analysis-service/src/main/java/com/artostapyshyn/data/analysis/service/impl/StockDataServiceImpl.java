package com.artostapyshyn.data.analysis.service.impl;

import com.artostapyshyn.data.analysis.config.MessageMapHolder;
import com.artostapyshyn.data.analysis.exceptions.StockDataNotFoundException;
import com.artostapyshyn.data.analysis.model.StockData;
import com.artostapyshyn.data.analysis.service.StockDataService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class StockDataServiceImpl implements StockDataService {

    private final ObjectMapper objectMapper;
    private final MessageMapHolder messageMapHolder;

    @Override
    public Mono<StockData> parseJson(String json) {
        return Mono.fromCallable(() -> objectMapper.readValue(json, StockData.class));
    }

    @Override
    public Mono<StockData> getStockDataFromQueue(String requestId) {
        return Mono.defer(() -> {
            String json = messageMapHolder.getStockDataMap().get(requestId);

            if (json == null) {
                return Mono.error(new StockDataNotFoundException("Failed to get StockData from RabbitMQ"));
            }

            return parseJson(json)
                    .onErrorMap(JsonProcessingException.class, e ->
                            new RuntimeException("Failed to parse JSON for StockData", e)
                    );
        });
    }
}
