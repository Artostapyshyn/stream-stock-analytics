package com.artostapyshyn.data.analysis.listeners;

import com.artostapyshyn.data.analysis.service.StockDataService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class FinancialDataListener {

    private final StockDataService stockDataService;

    @RabbitListener(queues = "financial-data-queue")
    public void handleMessage(@Payload String jsonMessage) throws JsonProcessingException {
        if (jsonMessage != null) {
            stockDataService.parseJson(jsonMessage);
        }
    }
}
