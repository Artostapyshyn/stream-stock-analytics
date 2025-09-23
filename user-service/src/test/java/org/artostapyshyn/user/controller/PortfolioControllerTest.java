package org.artostapyshyn.user.controller;

import org.artostapyshyn.user.model.Portfolio;
import org.artostapyshyn.user.service.PortfolioService;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PortfolioControllerTest {

    private final PortfolioService portfolioService = mock(PortfolioService.class);
    private final PortfolioController controller = new PortfolioController(portfolioService);

    @Test
    void testCreatePortfolio() {
        Portfolio portfolio = new Portfolio();
        when(portfolioService.save(portfolio)).thenReturn(Mono.just(portfolio));

        Portfolio result = controller.createPortfolio(portfolio).block();
        assertEquals(portfolio, result);
    }

    @Test
    void testGetAllPortfolios() {
        when(portfolioService.findAll()).thenReturn(Flux.just(new Portfolio()));

        List<Portfolio> result = controller.getAllPortfolios().collectList().block();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetPortfolioById_found() {
        Portfolio p = new Portfolio();
        when(portfolioService.findById(5L)).thenReturn(Mono.just(p));

        Portfolio result = controller.getPortfolioById(5L).block();
        assertNotNull(result);
        assertEquals(p, result);
    }

    @Test
    void testGetPortfolioById_notFound() {
        when(portfolioService.findById(5L)).thenReturn(Mono.empty());

        Portfolio result = controller.getPortfolioById(5L).block();
        assertNull(result);
    }

    @Test
    void testUpdatePortfolio() {
        Portfolio updated = new Portfolio();
        when(portfolioService.update(5L, updated)).thenReturn(Mono.just(updated));

        Portfolio result = controller.updatePortfolio(5L, updated).block();
        assertNotNull(result);
        assertEquals(updated, result);
    }

    @Test
    void testDeletePortfolio() {
        when(portfolioService.delete(5L)).thenReturn(Mono.empty());

        controller.deletePortfolio(5L).block();
        verify(portfolioService, times(1)).delete(5L);
    }
}
