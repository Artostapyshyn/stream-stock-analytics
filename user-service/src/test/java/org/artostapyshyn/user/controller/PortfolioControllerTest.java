package org.artostapyshyn.user.controller;

import org.artostapyshyn.user.model.Portfolio;
import org.artostapyshyn.user.service.PortfolioService;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PortfolioControllerTest {

    private final PortfolioService portfolioService = mock(PortfolioService.class);
    private final PortfolioController controller = new PortfolioController(portfolioService);

    @Test
    void testCreatePortfolio() {
        Portfolio portfolio = new Portfolio();
        when(portfolioService.save(portfolio)).thenReturn(portfolio);

        ResponseEntity<Portfolio> response = controller.createPortfolio(portfolio);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllPortfolios() {
        when(portfolioService.findAll()).thenReturn(List.of(new Portfolio()));

        ResponseEntity<List<Portfolio>> response = controller.getAllPortfolios();
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetPortfolioById_found() {
        Portfolio p = new Portfolio();
        when(portfolioService.findById("1")).thenReturn(Optional.of(p));

        ResponseEntity<Portfolio> response = controller.getPortfolioById("1");
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetPortfolioById_notFound() {
        when(portfolioService.findById("2")).thenReturn(Optional.empty());

        ResponseEntity<Portfolio> response = controller.getPortfolioById("2");
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testUpdatePortfolio() {
        Portfolio updated = new Portfolio();
        when(portfolioService.update("1", updated)).thenReturn(updated);

        ResponseEntity<Portfolio> response = controller.updatePortfolio("1", updated);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testDeletePortfolio() {
        doNothing().when(portfolioService).delete("1");

        ResponseEntity<Void> response = controller.deletePortfolio("1");
        assertEquals(204, response.getStatusCodeValue());
    }
}
