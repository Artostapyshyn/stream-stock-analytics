package org.artostapyshyn.user.repository;

import org.artostapyshyn.user.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {
}
