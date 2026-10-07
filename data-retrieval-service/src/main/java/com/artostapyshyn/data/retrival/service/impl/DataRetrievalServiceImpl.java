package com.artostapyshyn.data.retrival.service.impl;

import com.artostapyshyn.data.retrival.model.RequestStatistics;
import com.artostapyshyn.data.retrival.service.DataRetrievalService;
import com.artostapyshyn.data.retrival.service.FinancialDataSenderService;
import com.artostapyshyn.data.retrival.service.RequestStatisticsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataRetrievalServiceImpl implements DataRetrievalService {

    private static final String ALPHA_VANTAGE_URL = "https://www.alphavantage.co/query";
    private static final String REQUEST_ID_FIELD = "requestId";

    private final RequestStatisticsService requestStatisticsService;
    private final WebClient webClient;
    private final FinancialDataSenderService financialDataSenderService;
    private final ObjectMapper objectMapper;

    @Value("${alphavantage.apikey}")
    private String apiKey;

    @Override
    public Mono<Object> getData(String function, String symbol, String interval) {
        String uri = UriComponentsBuilder.fromUriString(ALPHA_VANTAGE_URL)
                .queryParam("function", function)
                .queryParam("symbol", symbol)
                .queryParam("interval", interval)
                .queryParam("apikey", apiKey)
                .toUriString();

        long startTime = System.nanoTime();

        RequestStatistics requestStatistics = new RequestStatistics();
        requestStatistics.setRequestType(symbol + " " + interval);
        requestStatistics.setTimestamp(LocalDateTime.now());

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(Object.class)
                .doOnNext(body -> {
                    requestStatistics.setResponseTime((System.nanoTime() - startTime) / 1_000_000);
                    requestStatisticsService.save(requestStatistics);

                    String requestId = UUID.randomUUID().toString();
                    financialDataSenderService.sendFinancialData(
                            addRequestId(body, requestId), requestId);
                });
    }

    private String addRequestId(Object body, String requestId) {
        try {
            JsonNode jsonNode = objectMapper.valueToTree(body);
            if (!jsonNode.isObject()) {
                throw new IllegalStateException("Alpha Vantage response must be a JSON object");
            }
            ObjectNode response = (ObjectNode) jsonNode;
            response.put(REQUEST_ID_FIELD, requestId);
            return objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException exception) {
            log.error("Unable to serialize Alpha Vantage response", exception);
            throw new IllegalStateException("Unable to serialize Alpha Vantage response", exception);
        }
    }
}
