package com.tech.ada.java.lojadeva.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository  extends JpaRepository<Client, Long> {
    Client findById(Long Id);
}
