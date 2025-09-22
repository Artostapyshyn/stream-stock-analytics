package com.artostapyshyn.data.retrival.service;

import com.artostapyshyn.data.retrival.service.impl.FinancialDataSenderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;

class FinancialDataSenderServiceImplTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private FinancialDataSenderServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendFinancialData_shouldSendMessageToQueue() {
        String data = "{\"key\":\"value\"}";
        String requestId = "12345";

        service.sendFinancialData(data, requestId);

        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
        verify(rabbitTemplate).send(eq("financial-data-queue"), messageCaptor.capture());

        Message sentMessage = messageCaptor.getValue();
        assert sentMessage.getMessageProperties().getHeaders().get("requestId").equals(requestId);
    }
}