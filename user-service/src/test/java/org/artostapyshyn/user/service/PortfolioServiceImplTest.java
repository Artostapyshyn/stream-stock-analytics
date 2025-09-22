package org.artostapyshyn.user.service;

import org.artostapyshyn.user.model.Portfolio;
import org.artostapyshyn.user.model.Stock;
import org.artostapyshyn.user.repository.PortfolioRepository;
import org.artostapyshyn.user.service.impl.PortfolioServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PortfolioServiceImplTest {

    private final PortfolioRepository portfolioRepository = mock(PortfolioRepository.class);
    private final PortfolioServiceImpl service = new PortfolioServiceImpl(portfolioRepository);

    @Test
    void testSave_withStocks() {
        Portfolio p = new Portfolio();
        Stock s = new Stock();
        p.setStocks(List.of(s));

        when(portfolioRepository.save(p)).thenReturn(p);

        Portfolio result = service.save(p);
        assertEquals(p, result);
        assertEquals(p, s.getPortfolio());
    }

    @Test
    void testFindAll() {
        when(portfolioRepository.findAll()).thenReturn(List.of(new Portfolio()));
        assertEquals(1, service.findAll().size());
    }

    @Test
    void testFindById_found() {
        Portfolio p = new Portfolio();
        when(portfolioRepository.findById("1")).thenReturn(Optional.of(p));
        assertTrue(service.findById("1").isPresent());
    }

    @Test
    void testFindById_notFound() {
        when(portfolioRepository.findById("1")).thenReturn(Optional.empty());
        assertTrue(service.findById("1").isEmpty());
    }

    @Test
    void testUpdate_success() {
        String id = "123";
        Portfolio existing = new Portfolio();
        existing.setId(id);
        existing.setName("Old");
        existing.setDescription("Old desc");
        existing.setStocks(new ArrayList<>());

        Portfolio updated = new Portfolio();
        updated.setName("New Name");
        updated.setDescription("New Description");
        List<Stock> updatedStocks = new ArrayList<>();
        Stock s1 = new Stock();
        s1.setTicker("AAPL");
        updatedStocks.add(s1);
        updated.setStocks(updatedStocks);

        when(portfolioRepository.findById(id)).thenReturn(Optional.of(existing));
        when(portfolioRepository.save(any(Portfolio.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Portfolio result = service.update(id, updated);

        assertEquals("New Name", result.getName());
        assertEquals("New Description", result.getDescription());
        assertEquals(1, result.getStocks().size());
        assertEquals("AAPL", result.getStocks().get(0).getTicker());
        assertEquals(existing, result.getStocks().get(0).getPortfolio());
    }

    @Test
    void testUpdate_portfolioNotFound() {
        when(portfolioRepository.findById("not_exist")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                service.update("not_exist", new Portfolio()));

        assertEquals("Portfolio not found", exception.getMessage());
    }

    @Test
    void testUpdate_notFound() {
        when(portfolioRepository.findById("99")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.update("99", new Portfolio()));
    }

    @Test
    void testDelete() {
        doNothing().when(portfolioRepository).deleteById("1");
        service.delete("1");
        verify(portfolioRepository, times(1)).deleteById("1");
    }
}

