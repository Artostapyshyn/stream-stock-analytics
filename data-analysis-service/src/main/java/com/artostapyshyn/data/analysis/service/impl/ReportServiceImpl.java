package com.artostapyshyn.data.analysis.service.impl;

import com.artostapyshyn.data.analysis.model.StockData;
import com.artostapyshyn.data.analysis.service.IndicatorCalculationService;
import com.artostapyshyn.data.analysis.service.ReportService;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
@AllArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final IndicatorCalculationService indicatorCalculationService;

    private static final String PDF_FORMAT = "pdf";
    private static final String XLSX_FORMAT = "xlsx";

    @Override
    public Mono<Resource> generateReport(StockData stockData, String format, List<String> indicators) {
        return calculateIndicators(stockData, indicators)
                .flatMap(indicatorResults -> switch (format) {
                    case PDF_FORMAT -> Mono.fromCallable(() -> generatePdfReport(stockData, indicatorResults));
                    case XLSX_FORMAT -> Mono.fromCallable(() -> generateXlsxReport(stockData, indicatorResults));
                    default -> Mono.error(new IllegalArgumentException("Invalid format"));
                });
    }

    private Resource generatePdfReport(StockData stockData, Map<String, Map<String, BigDecimal>> indicatorResults) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            document.open();

            document.add(new Paragraph("Stock Symbol: " + stockData.getMetaData().getSymbol()));
            document.add(new Paragraph("Last Refreshed: " + stockData.getMetaData().getLastRefreshed()));
            document.add(new Paragraph("Time Zone: " + stockData.getMetaData().getTimeZone()));
            document.add(new Paragraph("\n"));

            indicatorResults.forEach((indicator, values) -> {
                try {
                    document.add(new Paragraph(indicator));
                    values.forEach((key, value) -> {
                        try {
                            document.add(new Paragraph(key + ": " + value));
                        } catch (DocumentException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    document.add(new Paragraph("\n"));
                } catch (DocumentException e) {
                    throw new RuntimeException(e);
                }
            });

            document.close();
            return createResource(outputStream.toByteArray(), "report.pdf");
        } catch (IOException | DocumentException e) {
            log.error("Error generating PDF report", e);
            throw new RuntimeException(e);
        }
    }

    private Resource generateXlsxReport(StockData stockData, Map<String, Map<String, BigDecimal>> indicatorResults) {
        try (Workbook workbook = new HSSFWorkbook()) {
            indicatorResults.forEach((indicator, values) -> {
                Sheet sheet = workbook.createSheet(indicator);

                Row metaRow = sheet.createRow(0);
                metaRow.createCell(0).setCellValue("Stock Symbol");
                metaRow.createCell(1).setCellValue(stockData.getMetaData().getSymbol());

                Row lastRefRow = sheet.createRow(1);
                lastRefRow.createCell(0).setCellValue("Last Refreshed");
                lastRefRow.createCell(1).setCellValue(stockData.getMetaData().getLastRefreshed());

                Row tzRow = sheet.createRow(2);
                tzRow.createCell(0).setCellValue("Time Zone");
                tzRow.createCell(1).setCellValue(stockData.getMetaData().getTimeZone());

                int rowIndex = 4;
                for (Map.Entry<String, BigDecimal> entry : values.entrySet()) {
                    Row row = sheet.createRow(rowIndex++);
                    row.createCell(0).setCellValue(entry.getKey());
                    row.createCell(1).setCellValue(entry.getValue().toString());
                }
            });

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return createResource(outputStream.toByteArray(), "report.xlsx");
        } catch (IOException e) {
            log.error("Error generating XLSX report", e);
            throw new RuntimeException(e);
        }
    }

    private Resource createResource(byte[] data, String fileName) {
        return new ByteArrayResource(data) {
            @Override
            public String getFilename() {
                return fileName;
            }
        };
    }

    private Mono<Map<String, Map<String, BigDecimal>>> calculateIndicators(StockData stockData, List<String> indicators) {
        Map<String, Function<StockData, Mono<Map<String, BigDecimal>>>> indicatorFunctions = Map.of(
                "averagePrice", indicatorCalculationService::calculateAveragePrice,
                "priceChange", indicatorCalculationService::calculatePriceChange,
                "percentagePriceChange", indicatorCalculationService::calculatePercentagePriceChange,
                "averageVolume", indicatorCalculationService::calculateAverageVolume,
                "minPrice", indicatorCalculationService::calculateMinPrice,
                "maxPrice", indicatorCalculationService::calculateMaxPrice
        );

        List<Mono<Map.Entry<String, Map<String, BigDecimal>>>> monos = indicators.stream()
                .map(indicator -> {
                    Function<StockData, Mono<Map<String, BigDecimal>>> func = indicatorFunctions.get(indicator);
                    if (func == null) {
                        return Mono.<Map.Entry<String, Map<String, BigDecimal>>>error(
                                new IllegalArgumentException("Invalid indicator: " + indicator));
                    }
                    return func.apply(stockData)
                            .map(result -> Map.entry(indicator, result));
                }).toList();

        return Mono.zip(monos, array -> {
            Map<String, Map<String, BigDecimal>> result = new HashMap<>();
            for (Object obj : array) {
                @SuppressWarnings("unchecked")
                Map.Entry<String, Map<String, BigDecimal>> entry = (Map.Entry<String, Map<String, BigDecimal>>) obj;
                result.put(entry.getKey(), entry.getValue());
            }
            return result;
        });
    }

    public HttpHeaders getFileDownloadHeaders(String fileName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", fileName);
        return headers;
    }
}