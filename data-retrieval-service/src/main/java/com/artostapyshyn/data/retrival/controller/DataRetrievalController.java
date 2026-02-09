package com.artostapyshyn.data.retrival.controller;

import com.artostapyshyn.data.retrival.service.DataRetrievalService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/data-retrieval")
@RequiredArgsConstructor
public class DataRetrievalController {

    private final DataRetrievalService dataRetrievalService;

    @Operation(summary = "Get data from Alpha Vantage API")
    @GetMapping("/get-data")
    public Mono<Object> getData(@RequestParam String function,
                                @RequestParam String symbol,
                                @RequestParam String interval) {
        return dataRetrievalService.getData(function, symbol, interval);
    }
}
