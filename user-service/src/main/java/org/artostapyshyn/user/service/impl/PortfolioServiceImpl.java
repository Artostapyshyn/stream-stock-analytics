package org.artostapyshyn.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.artostapyshyn.user.model.Portfolio;
import org.artostapyshyn.user.repository.PortfolioRepository;
import org.artostapyshyn.user.service.PortfolioService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PortfolioServiceImpl implements PortfolioService {

    private final PortfolioRepository portfolioRepository;

    @Override
    public Portfolio save(Portfolio portfolio) {
        if (portfolio.getStocks() != null) {
            portfolio.getStocks().forEach(stock -> stock.setPortfolio(portfolio));
        }
        return portfolioRepository.save(portfolio);
    }

    @Override
    public List<Portfolio> findAll() {
        return portfolioRepository.findAll();
    }

    @Override
    public Optional<Portfolio> findById(String id) {
        return portfolioRepository.findById(id);
    }

    @Override
    public Portfolio update(String id, Portfolio updatedPortfolio) {
        return portfolioRepository.findById(id)
                .map(existing -> {
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
                })
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));
    }

    @Override
    public void delete(String id) {
        portfolioRepository.deleteById(id);
    }
}

