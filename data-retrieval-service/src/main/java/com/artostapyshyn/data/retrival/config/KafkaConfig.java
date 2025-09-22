package com.artostapyshyn.data.retrival.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@EnableKafka
public class KafkaConfig {

    public static final String FINANCIAL_DATA_TOPIC = "financial-data-topic";

    @Bean
    public NewTopic financialDataTopic() {
        return TopicBuilder
                .name(FINANCIAL_DATA_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
