package ru.ktelabs.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ktelabs.store.models.Position;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {
}
