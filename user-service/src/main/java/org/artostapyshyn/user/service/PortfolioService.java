package org.artostapyshyn.user.service;

import org.artostapyshyn.user.model.Portfolio;

import java.util.List;
import java.util.Optional;

public interface PortfolioService {
    Portfolio save(Portfolio portfolio);

    List<Portfolio> findAll();

    Optional<Portfolio> findById(String id);

    Portfolio update(String id, Portfolio updatedPortfolio);

    void delete(String id);
}
