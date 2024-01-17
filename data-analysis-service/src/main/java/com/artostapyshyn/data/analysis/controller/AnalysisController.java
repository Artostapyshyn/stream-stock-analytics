package com.artostapyshyn.data.analysis.controller;

import com.artostapyshyn.data.analysis.config.MessageMapHolder;
import com.artostapyshyn.data.analysis.exceptions.StockDataNotFoundException;
import com.artostapyshyn.data.analysis.model.StockData;
import com.artostapyshyn.data.analysis.service.IndicatorCalculationService;
import com.artostapyshyn.data.analysis.service.StockDataService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.function.Function;

@Log4j2
@RestController
@RequestMapping("/api/v1/data-analysis")
@AllArgsConstructor
public class AnalysisController {

    private final IndicatorCalculationService indicatorCalculationService;
    private final MessageMapHolder messageMapHolder;
    private final StockDataService stockDataService;

    private ResponseEntity<Object> handleStockDataRequest(String requestId, Function<StockData, Map<String, Double>> calculationFunction) {
        String json = messageMapHolder.getStockDataMap().get(requestId);

        if (json == null) {
            throw new StockDataNotFoundException("Failed to get StockData from RabbitMQ");
        }

        log.warn("Received stock data: {}", json);
        StockData stockData = null;
        try {
            stockData = stockDataService.parseJson(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
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

    @Operation(summary = "Calculate min price for given stock data with request id")
    @GetMapping("/min-price")
    public ResponseEntity<Object> calculateMinPrice(@RequestParam("requestId") String requestId) {
        return handleStockDataRequest(requestId, indicatorCalculationService::calculateMinPrice);
    }

    @Operation(summary = "Calculate max price for given stock data with request id")
    @GetMapping("/max-price")
    public ResponseEntity<Object> calculateMaxPrice(@RequestParam("requestId") String requestId) {
        return handleStockDataRequest(requestId, indicatorCalculationService::calculateMaxPrice);
    }
}
