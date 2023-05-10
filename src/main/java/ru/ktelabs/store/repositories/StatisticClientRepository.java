package ru.ktelabs.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ktelabs.store.models.StatisticClient;

@Repository
public interface StatisticClientRepository extends JpaRepository<StatisticClient, Long> {
}
