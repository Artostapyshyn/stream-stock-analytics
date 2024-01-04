package com.artostapyshyn.data.retrival.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue financialDataQueue() {
        return new Queue("financial-data-queue");
    }
}