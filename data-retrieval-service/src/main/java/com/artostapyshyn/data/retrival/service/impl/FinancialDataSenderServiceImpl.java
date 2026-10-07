package com.artostapyshyn.data.retrival.service.impl;

import com.artostapyshyn.data.retrival.service.FinancialDataSenderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class FinancialDataSenderServiceImpl implements FinancialDataSenderService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaSender<String, String> kafkaSender;
    private static final String TOPIC = "market.quotes.raw.v1";

    public FinancialDataSenderServiceImpl(KafkaTemplate<String, String> kafkaTemplate,
                                          KafkaSender<String, String> kafkaSender) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaSender = kafkaSender;
    }

    @Override
    public void sendFinancialData(String data, String requestId) {
        ProducerRecord<String, String> prodRecord = new ProducerRecord<>(TOPIC, data);
        prodRecord.headers().add(new RecordHeader("requestId", requestId.getBytes(StandardCharsets.UTF_8)));

        if (kafkaSender != null) {
            kafkaSender.send(Flux.just(SenderRecord.create(prodRecord, requestId)))
                    .doOnError(error -> log.error("Failed to send financial data to Kafka", error))
                    .subscribe();
            return;
        }

        kafkaTemplate.send(prodRecord);
    }
}
