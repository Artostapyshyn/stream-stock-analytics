package com.artostapyshyn.data.retrival.controller;

import com.artostapyshyn.data.retrival.service.DataRetrievalService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/data-retrieval")
@AllArgsConstructor
public class DataRetrievalController {

    private final DataRetrievalService dataRetrievalService;

    @Operation(summary = "Get data from Alpha Vantage API")
    @GetMapping("/get-data")
    public ResponseEntity<Object> getData(@RequestParam String function,
                                          @RequestParam String symbol,
                                          @RequestParam String interval) {
        return dataRetrievalService.getData(function, symbol, interval);
    }
}
