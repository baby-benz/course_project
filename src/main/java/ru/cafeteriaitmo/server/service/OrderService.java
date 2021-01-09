package ru.cafeteriaitmo.server.service;

import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import ru.cafeteriaitmo.server.controller.exception.NoEntityException;
import ru.cafeteriaitmo.server.domain.entity.Order;

import java.util.Collection;

public interface OrderService {
    Page<Order> getOrderPage(Long page);
    Collection<Order> getAll();
    Order getOrder(Long orderId) throws NoEntityException;
    Order createOrder(JSONObject valuesToCreateOrder) throws NoEntityException;
}
