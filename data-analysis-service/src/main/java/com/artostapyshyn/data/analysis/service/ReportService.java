package com.artostapyshyn.data.analysis.service;

import com.artostapyshyn.data.analysis.model.StockData;
import org.springframework.core.io.Resource;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ReportService {
    Mono<Resource> generateReport(StockData stockData, String format, List<String> indicators);
}
