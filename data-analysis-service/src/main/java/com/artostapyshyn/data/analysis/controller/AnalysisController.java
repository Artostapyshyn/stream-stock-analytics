package com.artostapyshyn.data.analysis.controller;

import com.artostapyshyn.data.analysis.exceptions.StockDataNotFoundException;
import com.artostapyshyn.data.analysis.model.StockData;
import com.artostapyshyn.data.analysis.service.IndicatorCalculationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@RestController
@RequestMapping("/api/data-analysis")
@AllArgsConstructor
public class AnalysisController {

    private final AmqpTemplate rabbitTemplate;
    private final IndicatorCalculationService indicatorCalculationService;

    private ResponseEntity<Object> handleStockDataRequest(String requestId, Function<StockData, Map<String, Double>> calculationFunction) {
        StockData stockData = Optional.ofNullable((StockData) rabbitTemplate.convertSendAndReceive("exchange_name", "routing_key", requestId))
                .orElseThrow(() -> new StockDataNotFoundException("Failed to get StockData from RabbitMQ"));

        Map<String, Double> result = calculationFunction.apply(stockData);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Calculate average price for given stock data with request id")
    @GetMapping("/average-price")
    public ResponseEntity<Object> calculateAveragePrice(@RequestParam("requestId") String requestId) {
        return handleStockDataRequest(requestId, indicatorCalculationService::calculateAveragePrice);
    }

    @Operation(summary = "Calculate price change for given stock data with request id")
    @GetMapping("/price-change")
    public ResponseEntity<Object> calculatePriceChange(@RequestParam("requestId") String requestId) {
        return handleStockDataRequest(requestId, indicatorCalculationService::calculatePriceChange);
    }

    @Operation(summary = "Calculate percentage price change for given stock data with request id")
    @GetMapping("/percentage-price-change")
    public ResponseEntity<Object> calculatePercentagePriceChange(@RequestParam("requestId") String requestId) {
        return handleStockDataRequest(requestId, indicatorCalculationService::calculatePercentagePriceChange);
    }

    @Operation(summary = "Calculate average volume change for given stock data with request id")
    @GetMapping("/average-volume")
    public ResponseEntity<Object> calculateAverageVolume(@RequestParam("requestId") String requestId) {
        return handleStockDataRequest(requestId, indicatorCalculationService::calculateAverageVolume);
    }

}
