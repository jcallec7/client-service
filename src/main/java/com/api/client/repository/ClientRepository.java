package com.api.client.repository;
import com.api.client.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client,Long> {

    Optional<Client> findByIdAndStatusIsTrue(Long id);
    boolean existsByIdentificationAndStatusIsTrue(String identification);
    boolean existsByClientIdAndStatusIsTrue(String clientId);

}
