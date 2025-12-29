package com.artostapyshyn.data.retrival.service.impl;

import com.artostapyshyn.data.retrival.service.FinancialDataSenderService;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FinancialDataSenderServiceImpl implements FinancialDataSenderService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "financial-data-topic";

    @Override
    public void sendFinancialData(String data, String requestId) {
        ProducerRecord<String, String> prodRecord = new ProducerRecord<>(TOPIC, data);
        prodRecord.headers().add(new RecordHeader("requestId", requestId.getBytes()));
        kafkaTemplate.send(prodRecord);
    }
}


