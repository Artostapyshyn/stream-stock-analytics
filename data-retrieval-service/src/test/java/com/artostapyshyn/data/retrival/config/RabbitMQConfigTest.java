package com.artostapyshyn.data.retrival.config;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;

import static org.junit.jupiter.api.Assertions.*;

class RabbitMQConfigTest {

    private final RabbitMQConfig config = new RabbitMQConfig();

    @Test
    void financialDataQueueIsCreated() {
        Queue queue = config.financialDataQueue();
        assertNotNull(queue);
        assertEquals("financial-data-queue", queue.getName());
        assertTrue(queue.isDurable());
    }

    @Test
    void financialDataExchangeIsCreated() {
        TopicExchange exchange = config.financialDataExchange();
        assertNotNull(exchange);
        assertEquals("financial-data-exchange", exchange.getName());
    }

    @Test
    void financialDataBindingIsCreatedCorrectly() {
        Queue queue = config.financialDataQueue();
        TopicExchange exchange = config.financialDataExchange();
        Binding binding = config.financialDataBinding(queue, exchange);

        assertNotNull(binding);
        assertEquals("financial-data", binding.getRoutingKey());
        assertEquals(queue.getName(), binding.getDestination());
    }
}
