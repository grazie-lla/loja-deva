package com.tech.ada.java.lojadeva.repository;

import com.tech.ada.java.lojadeva.domain.BasketItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasketItemRepository extends JpaRepository<BasketItem, Long> {
}
