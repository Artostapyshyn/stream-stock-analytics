package com.artostapyshyn.data.analysis.listeners;

import com.artostapyshyn.data.analysis.config.MessageMapHolder;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class FinancialDataListener {
    private final MessageMapHolder messageMapHolder;

    @RabbitListener(queues = "financial-data-queue", containerFactory = "rabbitListenerContainerFactory")
    public void handleMessage(@Payload String jsonMessage, @Header("requestId") String requestId) {
        if (jsonMessage != null && requestId != null) {
            messageMapHolder.getStockDataMap().put(requestId, jsonMessage);
        }
    }
}
