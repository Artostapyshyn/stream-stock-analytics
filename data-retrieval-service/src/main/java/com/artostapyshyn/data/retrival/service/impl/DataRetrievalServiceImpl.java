package com.artostapyshyn.data.retrival.service.impl;

import com.artostapyshyn.data.retrival.model.RequestStatistics;
import com.artostapyshyn.data.retrival.service.DataRetrievalService;
import com.artostapyshyn.data.retrival.service.FinancialDataSenderService;
import com.artostapyshyn.data.retrival.service.RequestStatisticsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataRetrievalServiceImpl implements DataRetrievalService {

    private static final String URL = "https://www.alphavantage.co/query";

    private final RequestStatisticsService requestStatisticsService;
    private final WebClient webClient;
    private final FinancialDataSenderService financialDataSenderService;
    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${alphavantage.apikey}")
    public String apikey;

    @Override
    public Mono<Object> getData(String function, String symbol, String interval) {
        String uri = UriComponentsBuilder.fromUriString(URL)
                .queryParam("function", function)
                .queryParam("symbol", symbol)
                .queryParam("interval", interval)
                .queryParam("apikey", apikey)
                .toUriString();

        long startTime = System.currentTimeMillis();

        RequestStatistics requestStatistics = new RequestStatistics();
        requestStatistics.setRequestType(symbol + " " + interval);
        requestStatistics.setTimestamp(LocalDateTime.now());

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(Object.class)
                .doOnNext(body -> {
                    long responseTime = System.currentTimeMillis() - startTime;
                    requestStatistics.setResponseTime(responseTime);
                    requestStatisticsService.save(requestStatistics);

                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        String jsonResponse = objectMapper.writeValueAsString(body);

                        String requestId = String.valueOf(secureRandom.nextInt(90000) + 10000);
                        jsonResponse = jsonResponse.substring(0, jsonResponse.length() - 1) +
                                ",\"requestId\":\"" + requestId + "\"}";

                        financialDataSenderService.sendFinancialData(jsonResponse, requestId);
                    } catch (Exception e) {
                        log.error("Error while processing response", e);
                    }
                });
    }
}
