package ru.ktelabs.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ktelabs.store.models.StatisticProduct;

@Repository
public interface StatisticProductRepository extends JpaRepository<StatisticProduct, Long> {
}
