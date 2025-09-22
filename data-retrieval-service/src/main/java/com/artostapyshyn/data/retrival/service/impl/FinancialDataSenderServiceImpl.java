package com.artostapyshyn.data.retrival.service.impl;

import com.artostapyshyn.data.retrival.service.FinancialDataSenderService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FinancialDataSenderServiceImpl implements FinancialDataSenderService {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void sendFinancialData(String data, String requestId) {
        MessageProperties properties = new MessageProperties();
        properties.setHeader("requestId", requestId);
        Message message = new Message(data.getBytes(), properties);
        rabbitTemplate.send("financial-data-queue", message);
    }
}


