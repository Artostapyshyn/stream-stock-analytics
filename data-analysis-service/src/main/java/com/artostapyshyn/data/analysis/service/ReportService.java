package com.artostapyshyn.data.analysis.service;

import com.artostapyshyn.data.analysis.model.StockData;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;

import java.util.List;

public interface ReportService {
    Resource generateReport(StockData stockData, String format, List<String> indicators);
    Resource generatePdfReport(StockData stockData, List<String> indicators);
    Resource generateXlsxReport(StockData stockData, List<String> indicators);
    HttpHeaders getFileDownloadHeaders(String fileName);
}
