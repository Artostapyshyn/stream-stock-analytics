package com.artostapyshyn.data.analysis.controller;

import com.artostapyshyn.data.analysis.dto.ReportRequestDTO;
import com.artostapyshyn.data.analysis.service.ReportService;
import com.artostapyshyn.data.analysis.service.StockDataService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/v1/report")
@AllArgsConstructor
public class ReportController {

    private final StockDataService stockDataService;
    private final ReportService reportService;

    @Operation(summary = "Generate report for given stock data with request id")
    @PostMapping("/generate")
    public Mono<ResponseEntity<Resource>> generateReport(@RequestBody ReportRequestDTO reportRequestDTO) {
        return stockDataService.getStockDataFromQueue(reportRequestDTO.requestId())
                .flatMap(stockData ->
                        reportService.generateReport(stockData, reportRequestDTO.format(), reportRequestDTO.indicators())
                                .map(report -> ResponseEntity.ok()
                                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + report.getFilename() + "\"")
                                        .body(report))
                )
                .doOnError(e -> log.error("Failed to generate report", e));
    }
}
