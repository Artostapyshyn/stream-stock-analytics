package org.artostapyshyn.user.controller;

import lombok.RequiredArgsConstructor;
import org.artostapyshyn.user.model.Portfolio;
import org.artostapyshyn.user.service.PortfolioService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/portfolios")
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioService portfolioService;

    @PostMapping
    public Mono<Portfolio> createPortfolio(@RequestBody Portfolio portfolio) {
        return portfolioService.save(portfolio);
    }

    @GetMapping
    public Flux<Portfolio> getAllPortfolios() {
        return portfolioService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Portfolio> getPortfolioById(@PathVariable Long id) {
        return portfolioService.findById(id);
    }

    @PutMapping("/{id}")
    public Mono<Portfolio> updatePortfolio(@PathVariable Long id, @RequestBody Portfolio portfolio) {
        return portfolioService.update(id, portfolio);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deletePortfolio(@PathVariable Long id) {
        return portfolioService.delete(id);
    }
}
