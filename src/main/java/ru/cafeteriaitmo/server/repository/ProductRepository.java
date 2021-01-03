package ru.cafeteriaitmo.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cafeteriaitmo.server.domain.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
