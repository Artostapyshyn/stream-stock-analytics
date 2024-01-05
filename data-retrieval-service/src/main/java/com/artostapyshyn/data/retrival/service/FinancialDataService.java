package com.artostapyshyn.data.retrival.service;

public interface FinancialDataService {
    void fetchDataAndSendToQueue(String data);
}
