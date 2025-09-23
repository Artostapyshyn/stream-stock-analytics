package org.artostapyshyn.user.repository;

import org.artostapyshyn.user.model.Portfolio;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface PortfolioRepository extends ReactiveCrudRepository<Portfolio, Long> {
}

