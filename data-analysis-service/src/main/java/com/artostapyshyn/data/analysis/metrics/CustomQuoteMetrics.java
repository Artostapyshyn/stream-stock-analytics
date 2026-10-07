package com.artostapyshyn.data.analysis.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Component
public class CustomQuoteMetrics {

    private final Counter processedQuotesCounter;
    private final Counter failedQuotesCounter;
    private final Timer quoteProcessingTimer;

    public CustomQuoteMetrics(MeterRegistry registry) {
        this.processedQuotesCounter = Counter.builder("quotes.processed.total")
                .description("Number of successfully processed quote events")
                .register(registry);
        this.failedQuotesCounter = Counter.builder("quotes.processing.failures.total")
                .description("Number of quote events that failed processing")
                .register(registry);
        this.quoteProcessingTimer = Timer.builder("quotes.processing.latency")
                .description("Quote processing latency")
                .publishPercentiles(0.5, 0.9, 0.99)
                .register(registry);
    }

    public <T> T recordProcessing(Supplier<T> processing) {
        long start = System.nanoTime();
        try {
            T result = processing.get();
            processedQuotesCounter.increment();
            return result;
        } catch (RuntimeException exception) {
            failedQuotesCounter.increment();
            throw exception;
        } finally {
            quoteProcessingTimer.record(System.nanoTime() - start, TimeUnit.NANOSECONDS);
        }
    }
}
