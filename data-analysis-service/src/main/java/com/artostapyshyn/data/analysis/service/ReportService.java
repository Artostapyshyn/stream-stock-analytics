package com.artostapyshyn.data.analysis.service;

import com.artostapyshyn.data.analysis.model.StockData;

import java.util.List;

public interface ReportService {
    byte[] generateReport(StockData stockData, String format, List<String> indicators);
    byte[] generatePdfReport(StockData stockData, List<String> indicators);
    byte[] generateXlsxReport(StockData stockData, List<String> indicators);
}
