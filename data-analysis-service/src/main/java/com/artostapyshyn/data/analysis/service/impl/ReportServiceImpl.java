package com.artostapyshyn.data.analysis.service.impl;

import com.artostapyshyn.data.analysis.model.StockData;
import com.artostapyshyn.data.analysis.service.IndicatorCalculationService;
import com.artostapyshyn.data.analysis.service.ReportService;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
@AllArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final IndicatorCalculationService indicatorCalculationService;
    private static final String PDF_FORMAT = "pdf";
    private static final String XLSX_FORMAT = "xlsx";

    @Override
    public byte[] generateReport(StockData stockData, String format, List<String> indicators) {
        return switch (format) {
            case PDF_FORMAT -> generatePdfReport(stockData, indicators);
            case XLSX_FORMAT -> generateXlsxReport(stockData, indicators);
            default -> throw new IllegalArgumentException("Invalid format");
        };
    }

    @Override
    public byte[] generatePdfReport(StockData stockData, List<String> indicators) {
        Map<String, Map<String, Double>> indicatorResults = calculateIndicators(stockData, indicators);

        Document document = new Document();
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            document.add(new Paragraph("Stock Symbol: " + stockData.getMetaData().getSymbol()));
            document.add(new Paragraph("Last Refreshed: " + stockData.getMetaData().getLastRefreshed()));
            document.add(new Paragraph("Time Zone: " + stockData.getMetaData().getTimeZone()));
            document.add(new Paragraph("\n"));

            for (Map.Entry<String, Map<String, Double>> entry : indicatorResults.entrySet()) {
                document.add(new Paragraph(entry.getKey()));

                for (Map.Entry<String, Double> data : entry.getValue().entrySet()) {
                    document.add(new Paragraph(data.getKey() + ": " + data.getValue()));
                }

                document.add(new Paragraph("\n"));
            }

            document.close();
            return outputStream.toByteArray();

        } catch (Exception e) {
            log.error("Error occurred while generating PDF report", e);
        }
        return null;
    }

    @Override
    public byte[] generateXlsxReport(StockData stockData, List<String> indicators) {
        Map<String, Map<String, Double>> indicatorResults = calculateIndicators(stockData, indicators);

        try (Workbook workbook = new HSSFWorkbook()) {
            for (Map.Entry<String, Map<String, Double>> entry : indicatorResults.entrySet()) {
                Sheet sheet = workbook.createSheet(entry.getKey());

                Row metaDataRow = sheet.createRow(0);
                metaDataRow.createCell(0).setCellValue("Stock Symbol");
                metaDataRow.createCell(1).setCellValue(stockData.getMetaData().getSymbol());

                Row lastRefreshedRow = sheet.createRow(1);
                lastRefreshedRow.createCell(0).setCellValue("Last Refreshed");
                lastRefreshedRow.createCell(1).setCellValue(stockData.getMetaData().getLastRefreshed());

                Row timeZoneRow = sheet.createRow(2);
                timeZoneRow.createCell(0).setCellValue("Time Zone");
                timeZoneRow.createCell(1).setCellValue(stockData.getMetaData().getTimeZone());

                sheet.createRow(3);

                int rowIndex = 4;
                for (Map.Entry<String, Double> data : entry.getValue().entrySet()) {
                    Row row = sheet.createRow(rowIndex++);
                    row.createCell(0).setCellValue(data.getKey());
                    row.createCell(1).setCellValue(data.getValue());
                }
            }

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                workbook.write(outputStream);
                return outputStream.toByteArray();
            }
        } catch (IOException e) {
            log.error("Error occurred while generating XLSX report", e);
        }
        return null;
    }


    public Map<String, Map<String, Double>> calculateIndicators(StockData stockData, List<String> indicators) {
        Map<String, Map<String, Double>> result = new HashMap<>();

        for (String indicator : indicators) {
            switch (indicator) {
                case "averagePrice" ->
                        result.put("averagePrice", indicatorCalculationService.calculateAveragePrice(stockData));
                case "priceChange" ->
                        result.put("priceChange", indicatorCalculationService.calculatePriceChange(stockData));
                case "percentagePriceChange" ->
                        result.put("percentagePriceChange", indicatorCalculationService.calculatePercentagePriceChange(stockData));
                case "averageVolume" ->
                        result.put("averageVolume", indicatorCalculationService.calculateAverageVolume(stockData));
                case "minPrice" -> result.put("minPrice", indicatorCalculationService.calculateMinPrice(stockData));
                case "maxPrice" -> result.put("maxPrice", indicatorCalculationService.calculateMaxPrice(stockData));
                default -> throw new IllegalArgumentException("Invalid indicator: " + indicator);
            }
        }

        return result;
    }
}
