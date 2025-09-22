package com.artostapyshyn.data.analysis.controller;

import com.artostapyshyn.data.analysis.model.StockData;
import com.artostapyshyn.data.analysis.service.IndicatorCalculationService;
import com.artostapyshyn.data.analysis.service.StockDataService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@RestController
@RequestMapping("/api/v1/data-analysis")
@AllArgsConstructor
public class AnalysisController {

    private final IndicatorCalculationService indicatorCalculationService;
    private final StockDataService stockDataService;

    private Mono<Map<String, BigDecimal>> handleStockDataRequestReactive(
            String requestId,
            Function<StockData, Map<String, BigDecimal>> calculationFunction
    ) {
        return stockDataService.getStockDataFromQueue(requestId)
                .map(calculationFunction)
                .doOnError(e -> log.error("Error processing requestId {}: {}", requestId, e.getMessage(), e));
    }

    @Operation(summary = "Calculate average price for given stock data with request id")
    @GetMapping("/average-price")
    public Mono<ResponseEntity<Map<String, BigDecimal>>> calculateAveragePrice(
            @RequestParam("requestId") String requestId
    ) {
        return handleStockDataRequestReactive(requestId, indicatorCalculationService::calculateAveragePrice);
    }

    @Operation(summary = "Calculate price change for given stock data with request id")
    @GetMapping("/price-change")
    public Mono<ResponseEntity<Map<String, BigDecimal>>> calculatePriceChange(
            @RequestParam("requestId") String requestId
    ) {
        return handleStockDataRequestReactive(requestId, indicatorCalculationService::calculatePriceChange);
    }

    @Operation(summary = "Calculate percentage price change for given stock data with request id")
    @GetMapping("/percentage-price-change")
    public Mono<ResponseEntity<Map<String, BigDecimal>>> calculatePercentagePriceChange(
            @RequestParam("requestId") String requestId
    ) {
        return handleStockDataRequestReactive(requestId, indicatorCalculationService::calculatePercentagePriceChange);
    }

    @Operation(summary = "Calculate average volume change for given stock data with request id")
    @GetMapping("/average-volume")
    public Mono<ResponseEntity<Map<String, BigDecimal>>> calculateAverageVolume(
            @RequestParam("requestId") String requestId
    ) {
        return handleStockDataRequestReactive(requestId, indicatorCalculationService::calculateAverageVolume);
    }

    @Operation(summary = "Calculate min price for given stock data with request id")
    @GetMapping("/min-price")
    public Mono<ResponseEntity<Map<String, BigDecimal>>> calculateMinPrice(
            @RequestParam("requestId") String requestId
    ) {
        return handleStockDataRequestReactive(requestId, indicatorCalculationService::calculateMinPrice);
    }

    @Operation(summary = "Calculate max price for given stock data with request id")
    @GetMapping("/max-price")
    public Mono<ResponseEntity<Map<String, BigDecimal>>> calculateMaxPrice(
            @RequestParam("requestId") String requestId
    ) {
        return handleStockDataRequestReactive(requestId, indicatorCalculationService::calculateMaxPrice);
    }
}
