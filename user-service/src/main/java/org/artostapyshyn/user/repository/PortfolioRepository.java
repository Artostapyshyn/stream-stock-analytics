package org.artostapyshyn.user.repository;

import org.artostapyshyn.user.model.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioRepository extends JpaRepository<Portfolio, String> {
}

