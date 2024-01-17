package com.artostapyshyn.data.analysis.controller;

import com.artostapyshyn.data.analysis.dto.ReportRequestDTO;
import com.artostapyshyn.data.analysis.exceptions.StockDataNotFoundException;
import com.artostapyshyn.data.analysis.model.StockData;
import com.artostapyshyn.data.analysis.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/report")
@AllArgsConstructor
public class ReportController {

    private final AmqpTemplate rabbitTemplate;
    private final ReportService reportService;

    @Operation(summary = "Generate report for given stock data with request id")
    @PostMapping("/generate")
    public ResponseEntity<Resource> generateReport(@RequestBody ReportRequestDTO reportRequestDTO) {
        StockData stockData = getStockDataFromQueue(reportRequestDTO.requestId());
        Resource report = reportService.generateReport(stockData, reportRequestDTO.format(), reportRequestDTO.indicators());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + report.getFilename() + "\"")
                .body(report);
    }

    private StockData getStockDataFromQueue(String requestId) {
        return Optional.ofNullable((StockData) rabbitTemplate.convertSendAndReceive("", "", requestId))
                .orElseThrow(() -> new StockDataNotFoundException("Failed to get StockData from RabbitMQ"));
    }

}
