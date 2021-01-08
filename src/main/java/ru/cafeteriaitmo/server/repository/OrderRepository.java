package ru.cafeteriaitmo.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.cafeteriaitmo.server.domain.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
