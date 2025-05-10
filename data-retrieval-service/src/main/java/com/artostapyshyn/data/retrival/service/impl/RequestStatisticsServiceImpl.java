package com.artostapyshyn.data.retrival.service.impl;

import com.artostapyshyn.data.retrival.dao.RequestStatisticsDAO;
import com.artostapyshyn.data.retrival.model.RequestStatistics;
import com.artostapyshyn.data.retrival.service.RequestStatisticsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RequestStatisticsServiceImpl implements RequestStatisticsService {

    private final RequestStatisticsDAO requestStatisticsDAO;

    @Override
    public void save(RequestStatistics requestStatistics) {
        requestStatisticsDAO.save(requestStatistics);
    }
}

