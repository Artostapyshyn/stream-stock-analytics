package com.artostapyshyn.data.retrival.service.impl;

import com.artostapyshyn.data.retrival.service.FinancialDataSenderService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FinancialDataSenderServiceImpl implements FinancialDataSenderService {

    private final RabbitTemplate rabbitTemplate;
    private final Queue queue;

    @Override
    public void sendFinancialData(String data) {
        rabbitTemplate.convertAndSend(queue.getName(), data);
    }
}
