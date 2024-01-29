package com.artostapyshyn.data.retrival.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue financialDataQueue() {
        return new Queue("financial-data-queue");
    }

    @Bean
    public TopicExchange financialDataExchange() {
        return new TopicExchange("financial-data-exchange");
    }

    @Bean
    public Binding financialDataBinding(Queue financialDataQueue, TopicExchange financialDataExchange) {
        return BindingBuilder
                .bind(financialDataQueue)
                .to(financialDataExchange)
                .with("financial-data");
    }
}