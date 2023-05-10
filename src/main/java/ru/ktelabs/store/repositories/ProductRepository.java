package ru.ktelabs.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.ktelabs.store.models.Product;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT * FROM products ORDER BY random() LIMIT 1",   nativeQuery = true)
    Optional<Product> getRandomProduct();
}
