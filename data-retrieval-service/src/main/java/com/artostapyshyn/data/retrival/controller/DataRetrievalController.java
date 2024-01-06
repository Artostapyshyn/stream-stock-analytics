package com.artostapyshyn.data.retrival.controller;

import com.artostapyshyn.data.retrival.service.DataRetrievalService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/data-retrieval")
@AllArgsConstructor
public class DataRetrievalController {

    private final DataRetrievalService dataRetrievalService;

    @GetMapping("/get-data")
    public ResponseEntity<Object> getData(@RequestParam String symbol,
                                          @RequestParam String interval,
                                          @RequestParam String apiKey) {
        return dataRetrievalService.getData(symbol, interval, apiKey);
    }
}
