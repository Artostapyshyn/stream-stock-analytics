package org.artostapyshyn.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.artostapyshyn.user.model.Portfolio;
import org.artostapyshyn.user.repository.PortfolioRepository;
import org.artostapyshyn.user.service.PortfolioService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PortfolioServiceImpl implements PortfolioService {

    private final PortfolioRepository portfolioRepository;

    @Override
    public Mono<Portfolio> save(Portfolio portfolio) {
        if (portfolio.getStocks() != null) {
            portfolio.getStocks().forEach(stock -> stock.setPortfolio(portfolio));
        }
        return portfolioRepository.save(portfolio);
    }

    @Override
    public Flux<Portfolio> findAll() {
        return portfolioRepository.findAll();
    }

    @Override
    public Mono<Portfolio> findById(Long id) {
        return portfolioRepository.findById(id);
    }

    @Override
    public Mono<Portfolio> update(Long id, Portfolio updatedPortfolio) {
        return portfolioRepository.findById(id)
                .flatMap(existing -> {
                    existing.setName(updatedPortfolio.getName());
                    existing.setDescription(updatedPortfolio.getDescription());
                    existing.getStocks().clear();
                    if (updatedPortfolio.getStocks() != null) {
                        updatedPortfolio.getStocks().forEach(stock -> {
                            stock.setPortfolio(existing);
                            existing.getStocks().add(stock);
                        });
                    }
                    return portfolioRepository.save(existing);
                });
    }

    @Override
    public Mono<Void> delete(Long id) {
        portfolioRepository.deleteById(id);
        return null;
    }
}

