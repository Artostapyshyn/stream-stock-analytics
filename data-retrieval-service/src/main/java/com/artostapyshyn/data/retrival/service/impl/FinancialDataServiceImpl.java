package com.artostapyshyn.data.retrival.service.impl;

import com.artostapyshyn.data.retrival.service.FinancialDataSenderService;
import com.artostapyshyn.data.retrival.service.FinancialDataService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FinancialDataServiceImpl implements FinancialDataService {

    private final FinancialDataSenderService financialDataSenderService;

    @Override
    public void fetchDataAndSendToQueue(String data) {
        financialDataSenderService.sendFinancialData(data);
    }
}
