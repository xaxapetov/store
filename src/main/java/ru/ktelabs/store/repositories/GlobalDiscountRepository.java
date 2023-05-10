package ru.ktelabs.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ktelabs.store.models.GlobalDiscount;
import ru.ktelabs.store.models.Product;

import java.util.Optional;

@Repository
public interface GlobalDiscountRepository extends JpaRepository<GlobalDiscount, Long> {
    Optional<GlobalDiscount> findByProduct(Product product);
}
