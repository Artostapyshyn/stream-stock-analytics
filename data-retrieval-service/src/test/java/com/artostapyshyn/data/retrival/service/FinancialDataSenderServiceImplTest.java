package com.artostapyshyn.data.retrival.service;

import com.artostapyshyn.data.retrival.service.impl.FinancialDataSenderServiceImpl;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FinancialDataSenderServiceImplTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private FinancialDataSenderServiceImpl service;

    @Test
    void sendFinancialData_shouldSendMessageToKafkaTopic() {
        String data = "{\"key\":\"value\"}";
        String requestId = "12345";

        service.sendFinancialData(data, requestId);

        @SuppressWarnings({"rawtypes", "unchecked"})
        ArgumentCaptor<ProducerRecord<String, String>> recordCaptor = (ArgumentCaptor) ArgumentCaptor.forClass(ProducerRecord.class);
        verify(kafkaTemplate).send(recordCaptor.capture());

        ProducerRecord<String, String> sentRecord = recordCaptor.getValue();
        assertEquals("financial-data-topic", sentRecord.topic());

        byte[] headerValue = sentRecord.headers().lastHeader("requestId").value();
        assertEquals(requestId, new String(headerValue));
        assertEquals(data, sentRecord.value());
    }
}