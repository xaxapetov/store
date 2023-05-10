package ru.ktelabs.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ktelabs.store.models.Client;
import ru.ktelabs.store.models.Sale;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    Optional<Sale> findFirstByDateOfSaleBetweenOrderByChequeDesc(LocalDateTime from, LocalDateTime to);

    List<Sale> findAllByClient(Client client);

    default Optional<Sale> findByDateOrderByChequeDesc(LocalDate localDate) {
        return findFirstByDateOfSaleBetweenOrderByChequeDesc(localDate.atStartOfDay(), localDate.plusDays(1).atStartOfDay());
    }
}
