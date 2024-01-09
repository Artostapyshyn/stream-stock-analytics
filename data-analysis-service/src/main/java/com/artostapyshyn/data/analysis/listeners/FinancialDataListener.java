package com.artostapyshyn.data.analysis.listeners;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class FinancialDataListener {

    @RabbitListener(queues = "financial_data")
    public void handleMessage(String json) {
    }
}
