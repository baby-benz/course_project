package ru.cafeteriaitmo.server.service;

import org.json.simple.JSONArray;
import org.springframework.data.domain.Page;
import ru.cafeteriaitmo.server.domain.entity.Order;

import java.util.Collection;
import java.util.Optional;

public interface OrderService {
    Page<Order> getOrderPage(Long page);
    Collection<Order> getAll();
    Optional<Order> getOrder(Long orderId);
    Order createOrder(JSONArray valuesToCreateOrder);
}
