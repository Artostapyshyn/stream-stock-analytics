package org.artostapyshyn.user.service;

import org.artostapyshyn.user.model.Portfolio;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PortfolioService {
    Mono<Portfolio> save(Portfolio portfolio);

    Flux<Portfolio> findAll();

    Mono<Portfolio> findById(String id);

    Mono<Portfolio> update(String id, Portfolio updatedPortfolio);

    Mono<Void> delete(String id);
}
