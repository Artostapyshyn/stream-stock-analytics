package com.artostapyshyn.data.retrival.service.impl;

import com.artostapyshyn.data.retrival.model.RequestStatistics;
import com.artostapyshyn.data.retrival.service.DataRetrievalService;
import com.artostapyshyn.data.retrival.service.FinancialDataSenderService;
import com.artostapyshyn.data.retrival.service.RequestStatisticsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class DataRetrievalServiceImpl implements DataRetrievalService {

    private final RequestStatisticsService requestStatisticsService;
    private final RestTemplate restTemplate;
    private final static String URL = "https://www.alphavantage.co/query";
    private final FinancialDataSenderService financialDataSenderService;

    @Override
    public ResponseEntity<Object> getData(String function, String symbol, String interval) {

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL)
                .queryParam("function", function)
                .queryParam("symbol", symbol)
                .queryParam("interval", interval)
                .queryParam("apikey", "C656F9GOXQS3O9BI");

        long startTime = System.currentTimeMillis();
        RequestStatistics requestStatistics = new RequestStatistics();
        requestStatistics.setRequestType(symbol + " " + interval);
        requestStatistics.setTimestamp(LocalDateTime.now());

        ResponseEntity<Object> responseEntity = restTemplate.exchange(
                builder.toUriString(),
                org.springframework.http.HttpMethod.GET,
                null,
                Object.class);

        long responseTime = System.currentTimeMillis() - startTime;
        requestStatistics.setResponseTime(responseTime);

        requestStatisticsService.save(requestStatistics);

        if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
            String jsonResponse = responseEntity.getBody().toString();
            String requestId = UUID.randomUUID().toString();
            jsonResponse = jsonResponse.substring(0, jsonResponse.length() - 1) + ",\"requestId\":\"" + requestId + "\"}";
            financialDataSenderService.sendFinancialData(jsonResponse, requestId);
        }

        return responseEntity;
    }
}
