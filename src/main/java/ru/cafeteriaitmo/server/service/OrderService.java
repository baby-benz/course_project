package ru.cafeteriaitmo.server.service;

import org.springframework.data.domain.Page;
import ru.cafeteriaitmo.server.controller.exception.NoEntityException;
import ru.cafeteriaitmo.server.domain.entity.Order;
import ru.cafeteriaitmo.server.domain.enums.Status;
import ru.cafeteriaitmo.server.dto.OrderDto;

import java.util.Collection;

public interface OrderService {
    Page<OrderDto> getOrderPage(Long page);
    Collection<Order> getAll();
    OrderDto getOrderDto(Long orderId) throws NoEntityException;
    Order addOrder(Order order);
    Order addOrderDto(OrderDto order) throws NoEntityException;

    Order changeStatus(Long id, Status status) throws NoEntityException;
    OrderDto changeStatusString(Long id, String status) throws NoEntityException;
    Integer getNumberOfPages();

    @Deprecated
    Order getOrder(Long orderId) throws NoEntityException;
}
