package com.tech.ada.java.lojadeva.repository;

import com.tech.ada.java.lojadeva.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findById(Long id);

    Client findByCpf(String cpf);
}