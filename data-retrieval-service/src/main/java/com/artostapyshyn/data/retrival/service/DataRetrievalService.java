package com.artostapyshyn.data.retrival.service;

import reactor.core.publisher.Mono;

public interface DataRetrievalService {
    Mono<Object> getData(String function, String symbol, String interval);
}
