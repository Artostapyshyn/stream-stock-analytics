package com.artostapyshyn.data.retrival.service;

import org.springframework.http.ResponseEntity;

public interface DataRetrievalService {
    ResponseEntity<Object> getData(String function, String symbol, String interval);
}
