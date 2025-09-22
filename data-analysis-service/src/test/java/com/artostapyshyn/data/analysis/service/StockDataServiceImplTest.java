
package com.artostapyshyn.data.analysis.service;

import com.artostapyshyn.data.analysis.config.MessageMapHolder;
import com.artostapyshyn.data.analysis.exceptions.StockDataNotFoundException;
import com.artostapyshyn.data.analysis.model.StockData;
import com.artostapyshyn.data.analysis.service.impl.StockDataServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StockDataServiceImplTest {

    @Test
    void getStockDataFromQueue_ReturnsStockData() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StockData stockData = new StockData();
        String json = mapper.writeValueAsString(stockData);

        Map<String, String> map = new HashMap<>();
        map.put("test-id", json);
        MessageMapHolder holder = new MessageMapHolder();
        holder.getStockDataMap().putAll(map);

        StockDataServiceImpl service = new StockDataServiceImpl(mapper, holder);
        assertNotNull(service.getStockDataFromQueue("test-id"));
    }

    @Test
    void getStockDataFromQueue_ThrowsExceptionForMissingId() {
        ObjectMapper mapper = new ObjectMapper();
        MessageMapHolder holder = new MessageMapHolder();
        StockDataServiceImpl service = new StockDataServiceImpl(mapper, holder);

        assertThrows(StockDataNotFoundException.class, () -> service.getStockDataFromQueue("missing-id"));
    }
}
