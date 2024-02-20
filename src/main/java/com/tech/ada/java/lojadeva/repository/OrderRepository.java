package com.tech.ada.java.lojadeva.repository;

import com.tech.ada.java.lojadeva.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    public List<Order> findOrderByClientId(Long clientId);

}
