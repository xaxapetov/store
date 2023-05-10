package ru.ktelabs.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;
import ru.ktelabs.store.models.Client;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Optional;


@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "500")})
    Optional<Client> findClientById(Long id);

}
