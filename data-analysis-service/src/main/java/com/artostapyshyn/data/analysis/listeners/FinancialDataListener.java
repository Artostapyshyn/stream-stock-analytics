package com.artostapyshyn.data.analysis.listeners;

import com.artostapyshyn.data.analysis.config.MessageMapHolder;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class FinancialDataListener {
    private final MessageMapHolder messageMapHolder;

    @KafkaListener(topics = "financial-data-topic", groupId = "financial-data-group")
    public void handleMessage(@Payload String jsonMessage, @Header("requestId") String requestId) {
        if (jsonMessage != null && requestId != null) {
            messageMapHolder.getStockDataMap().put(requestId, jsonMessage);
        }
    }
}
