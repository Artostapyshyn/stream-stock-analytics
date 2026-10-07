package com.artostapyshyn.data.retrival.config;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

    public static final String FINANCIAL_DATA_TOPIC = "financial-data-topic";

    @Value("${spring.kafka.bootstrap-servers:kafka:9092}")
    private String bootstrapServers;

    @Bean
    public NewTopic rawQuotesTopic() {
        return TopicBuilder.name("market.quotes.raw.v1")
                .partitions(6)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic financialDataTopic() {
        return TopicBuilder.name(FINANCIAL_DATA_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public SenderOptions<String, String> quoteSenderOptions() {
        Map<String, Object> props = new HashMap<>();
        props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        return SenderOptions.create(props);
    }

    @Bean
    public KafkaSender<String, String> quoteKafkaSender(SenderOptions<String, String> quoteSenderOptions) {
        return KafkaSender.create(quoteSenderOptions);
    }

}
