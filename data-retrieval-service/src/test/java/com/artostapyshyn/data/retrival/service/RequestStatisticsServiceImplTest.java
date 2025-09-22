package com.artostapyshyn.data.retrival.service;

import com.artostapyshyn.data.retrival.dao.RequestStatisticsDAO;
import com.artostapyshyn.data.retrival.model.RequestStatistics;
import com.artostapyshyn.data.retrival.service.impl.RequestStatisticsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

class RequestStatisticsServiceImplTest {

    @Mock
    private RequestStatisticsDAO dao;

    @InjectMocks
    private RequestStatisticsServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_shouldCallDaoSave() {
        RequestStatistics stats = new RequestStatistics();
        service.save(stats);
        verify(dao).save(stats);
    }
}
