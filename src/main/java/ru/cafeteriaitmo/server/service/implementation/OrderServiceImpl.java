package ru.cafeteriaitmo.server.service.implementation;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.cafeteriaitmo.server.domain.entity.Order;
import ru.cafeteriaitmo.server.repository.OrderRepository;
import ru.cafeteriaitmo.server.service.OrderService;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    public Page<Order> getOrderPage(Long numberOfPage) {
        if (numberOfPage < 0L) return null;
        Pageable pageable = PageRequest.of(numberOfPage.intValue(), 5);
        return orderRepository.findAll(pageable);
    }

    public Optional<Order> getOrder(Long orderId) {
        return orderRepository.findById(orderId);
    }

    public Collection<Order> getAll() {
        return orderRepository.findAll();
    }

    public Order createOrder(JSONArray valuesToCreateOrder) {
        return null;
    }

}
