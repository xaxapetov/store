package ru.ktelabs.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ktelabs.store.models.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
}
