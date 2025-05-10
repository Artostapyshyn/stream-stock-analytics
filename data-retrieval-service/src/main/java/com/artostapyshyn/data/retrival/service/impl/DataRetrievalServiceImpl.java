package com.artostapyshyn.data.retrival.service.impl;

import com.artostapyshyn.data.retrival.model.RequestStatistics;
import com.artostapyshyn.data.retrival.service.DataRetrievalService;
import com.artostapyshyn.data.retrival.service.FinancialDataSenderService;
import com.artostapyshyn.data.retrival.service.RequestStatisticsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Log4j2
@Service
@RequiredArgsConstructor
public class DataRetrievalServiceImpl implements DataRetrievalService {
    private static final String URL = "https://www.alphavantage.co/query";

    private final RequestStatisticsService requestStatisticsService;
    private final RestTemplate restTemplate;

    private final FinancialDataSenderService financialDataSenderService;
    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${alphavantage.apikey}")
    public String apikey;


    @SneakyThrows
    @Override
    public ResponseEntity<Object> getData(String function, String symbol, String interval) {

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL)
                .queryParam("function", function)
                .queryParam("symbol", symbol)
                .queryParam("interval", interval)
                .queryParam("apikey", apikey);

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
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(responseEntity.getBody());

            String requestId = String.valueOf(secureRandom.nextInt(90000) + 10000);
            jsonResponse = jsonResponse.substring(0, jsonResponse.length() - 1) + ",\"requestId\":\"" + requestId + "\"}";

            financialDataSenderService.sendFinancialData(jsonResponse, requestId);
        }

        return responseEntity;
    }
}
