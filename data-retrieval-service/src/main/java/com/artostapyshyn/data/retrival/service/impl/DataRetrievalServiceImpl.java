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

@Service
@AllArgsConstructor
public class DataRetrievalServiceImpl implements DataRetrievalService {

    private final RequestStatisticsService requestStatisticsService;
    private final RestTemplate restTemplate;
    private final static String URL = "https://www.alphavantage.co/query";
    private final FinancialDataSenderService financialDataSenderService;

    @Override
    public ResponseEntity<Object> getData(String symbol, String interval, String apiKey) {

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL)
                .queryParam("function", "TIME_SERIES_INTRADAY")
                .queryParam("symbol", symbol)
                .queryParam("interval", interval)
                .queryParam("apikey", apiKey);

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
            financialDataSenderService.sendFinancialData(jsonResponse);
        }

        return responseEntity;
    }
}
