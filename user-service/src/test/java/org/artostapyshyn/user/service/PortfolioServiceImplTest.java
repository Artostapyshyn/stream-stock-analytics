package org.artostapyshyn.user.service;

import org.artostapyshyn.user.model.Portfolio;
import org.artostapyshyn.user.model.Stock;
import org.artostapyshyn.user.repository.PortfolioRepository;
import org.artostapyshyn.user.service.impl.PortfolioServiceImpl;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PortfolioServiceImplTest {

    private final PortfolioRepository portfolioRepository = mock(PortfolioRepository.class);
    private final PortfolioServiceImpl service = new PortfolioServiceImpl(portfolioRepository);

    @Test
    void testSave_withStocks() {
        Portfolio p = new Portfolio();
        Stock s = new Stock();
        p.setStocks(List.of(s));

        when(portfolioRepository.save(p)).thenReturn(Mono.just(p));

        Portfolio result = service.save(p).block();
        assertEquals(p, result);
        assertEquals(p, s.getPortfolio());
    }

    @Test
    void testFindAll() {
        when(portfolioRepository.findAll()).thenReturn(Flux.just(new Portfolio()));
        List<Portfolio> portfolios = service.findAll().collectList().block();
        assertNotNull(portfolios);
        assertEquals(1, portfolios.size());
    }

    @Test
    void testFindById_found() {
        Portfolio p = new Portfolio();
        when(portfolioRepository.findById(5L)).thenReturn(Mono.just(p));
        assertNotNull(service.findById(5L).block());
    }

    @Test
    void testFindById_notFound() {
        when(portfolioRepository.findById(5L)).thenReturn(Mono.empty());
        assertTrue(service.findById(5L).blockOptional().isEmpty());
    }
}