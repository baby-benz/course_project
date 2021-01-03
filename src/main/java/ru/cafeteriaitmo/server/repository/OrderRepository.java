package ru.cafeteriaitmo.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cafeteriaitmo.server.domain.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
