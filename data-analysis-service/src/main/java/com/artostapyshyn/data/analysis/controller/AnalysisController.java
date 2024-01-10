package com.artostapyshyn.data.analysis.controller;

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

@RestController
@RequestMapping("/api/data-analysis")
@AllArgsConstructor
public class AnalysisController {

    private final AmqpTemplate rabbitTemplate;
    private final IndicatorCalculationService indicatorCalculationService;

    @Operation(summary = "Calculate average price for given stock data with request id")
    @GetMapping("/average-price")
    public ResponseEntity<Object> calculateAveragePrice(@RequestParam("requestId") String requestId) {
        StockData stockData = (StockData) rabbitTemplate.convertSendAndReceive("exchange_name", "routing_key", requestId);

        if (stockData != null) {
            double averagePrice = indicatorCalculationService.calculateAveragePrice(stockData);
            return ResponseEntity.ok(averagePrice);
        } else {
            return ResponseEntity.status(500).body("Failed to get StockData from RabbitMQ");
        }
    }
}
