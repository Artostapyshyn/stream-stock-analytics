package com.artostapyshyn.data.retrival.service.impl;

import com.artostapyshyn.data.retrival.service.DataRetrievalService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@AllArgsConstructor
public class DataRetrievalServiceImpl implements DataRetrievalService {

    private final RestTemplate restTemplate;
    private final static String URL = "https://www.alphavantage.co/query";

    @Override
    public ResponseEntity<Object> getData(String symbol, String interval, String apiKey) {

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL)
                .queryParam("function", "TIME_SERIES_INTRADAY")
                .queryParam("symbol", symbol)
                .queryParam("interval", interval)
                .queryParam("apikey", apiKey);

        return restTemplate.exchange(
                builder.toUriString(),
                org.springframework.http.HttpMethod.GET,
                null,
                Object.class);
    }
}
