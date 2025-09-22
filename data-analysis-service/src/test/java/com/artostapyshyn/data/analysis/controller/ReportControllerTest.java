
package com.artostapyshyn.data.analysis.controller;

import com.artostapyshyn.data.analysis.dto.ReportRequestDTO;
import com.artostapyshyn.data.analysis.model.StockData;
import com.artostapyshyn.data.analysis.service.ReportService;
import com.artostapyshyn.data.analysis.service.StockDataService;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ReportControllerTest {

    @Test
    void generateReport_ReturnsResource() {
        StockDataService stockDataService = mock(StockDataService.class);
        ReportService reportService = mock(ReportService.class);
        ReportController controller = new ReportController(stockDataService, reportService);

        ReportRequestDTO dto = new ReportRequestDTO("req123", "pdf", List.of("averagePrice"));
        StockData mockData = new StockData();
        Resource mockResource = new ByteArrayResource("report".getBytes()) {
            @Override
            public String getFilename() {
                return "report.pdf";
            }
        };

        when(stockDataService.getStockDataFromQueue(dto.requestId())).thenReturn(mockData);
        when(reportService.generateReport(mockData, dto.format(), dto.indicators())).thenReturn(mockResource);

        ResponseEntity<Resource> response = controller.generateReport(dto);
        assertEquals("report.pdf", response.getBody().getFilename());
    }
}
