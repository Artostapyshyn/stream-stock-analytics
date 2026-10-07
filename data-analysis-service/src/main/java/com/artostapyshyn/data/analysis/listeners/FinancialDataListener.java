package com.artostapyshyn.data.analysis.listeners;

import com.artostapyshyn.data.analysis.config.MessageMapHolder;
import com.artostapyshyn.data.analysis.metrics.CustomQuoteMetrics;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverRecord;

@Component
@Slf4j
public class FinancialDataListener {

    private final MessageMapHolder messageMapHolder;
    private final CustomQuoteMetrics quoteMetrics;
    private final KafkaReceiver<String, String> kafkaReceiver;
    private Disposable subscription;

    public FinancialDataListener(MessageMapHolder messageMapHolder,
                                 CustomQuoteMetrics quoteMetrics,
                                 KafkaReceiver<String, String> kafkaReceiver) {
        this.messageMapHolder = messageMapHolder;
        this.quoteMetrics = quoteMetrics;
        this.kafkaReceiver = kafkaReceiver;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startConsumption() {
        subscription = consumeQuotesStream().subscribe(
                ignored -> {
                },
                error -> log.error("Reactive Kafka consumer stopped", error));
    }

    public Flux<Void> consumeQuotesStream() {
        return kafkaReceiver.receive()
                .flatMap(this::processRecord, 10);
    }

    private Mono<Void> processRecord(ReceiverRecord<String, String> record) {
        return Mono.fromRunnable(() -> {
                    String requestId = record.headers().lastHeader("requestId") == null
                            ? record.key()
                            : new String(record.headers().lastHeader("requestId").value(),
                            java.nio.charset.StandardCharsets.UTF_8);
                    if (record.value() == null || requestId == null) {
                        throw new IllegalArgumentException("Kafka record must contain a value and requestId");
                    }
                    quoteMetrics.recordProcessing(() -> {
                        messageMapHolder.getStockDataMap().put(requestId, record.value());
                        return null;
                    });
                })
                .doOnSuccess(ignored -> record.receiverOffset().acknowledge())
                .doOnError(error -> log.error("Error processing financial data record", error))
                .then();
    }

    @PreDestroy
    public void stopConsumption() {
        if (subscription != null) {
            subscription.dispose();
        }
    }
}
