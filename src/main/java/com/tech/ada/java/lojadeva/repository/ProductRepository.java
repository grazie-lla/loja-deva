package com.tech.ada.java.lojadeva.repository;

import com.tech.ada.java.lojadeva.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
