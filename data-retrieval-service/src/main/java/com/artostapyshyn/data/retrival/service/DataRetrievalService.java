package com.artostapyshyn.data.retrival.service;

import org.springframework.http.ResponseEntity;

public interface DataRetrievalService {
    ResponseEntity<Object> getData(String symbol, String interval, String apiKey);
}
