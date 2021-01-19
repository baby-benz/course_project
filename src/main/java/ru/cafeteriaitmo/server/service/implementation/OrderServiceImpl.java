package ru.cafeteriaitmo.server.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.cafeteriaitmo.server.controller.exception.NoEntityException;
import ru.cafeteriaitmo.server.domain.entity.Building;
import ru.cafeteriaitmo.server.domain.entity.Order;
import ru.cafeteriaitmo.server.domain.entity.Product;
import ru.cafeteriaitmo.server.domain.entity.User;
import ru.cafeteriaitmo.server.domain.enums.Status;
import ru.cafeteriaitmo.server.dto.OrderDto;
import ru.cafeteriaitmo.server.dto.ProductDto;
import ru.cafeteriaitmo.server.repository.OrderRepository;
import ru.cafeteriaitmo.server.repository.UserRepository;
import ru.cafeteriaitmo.server.service.BuildingService;
import ru.cafeteriaitmo.server.service.OrderService;
import ru.cafeteriaitmo.server.service.ProductService;
import ru.cafeteriaitmo.server.service.UserService;
import ru.cafeteriaitmo.server.service.helper.OrderConverter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;

    private final BuildingService buildingService;
    private  final ProductService productService;

    @Value("${cafeteria.api.pages.size}")
    private Integer pagesSize;

    public Page<OrderDto> getOrderPage(Long pageNumber) {
        if (pageNumber < 0L) return null;
        Pageable pageable = PageRequest.of(pageNumber.intValue(), pagesSize);
        Page<Order> orderPage = orderRepository.findAll(pageable);
        return OrderConverter.changePageToDtoPage(orderPage);
    }

    public Order getOrder(Long orderId) throws NoEntityException {
        return orderRepository.findById(orderId).orElseThrow(() ->
                new NoEntityException(Order.class.getSimpleName().toLowerCase(), orderId));
    }

    public OrderDto getOrderDto(Long orderId) throws NoEntityException {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new NoEntityException(Order.class.getSimpleName().toLowerCase(), orderId));
        return OrderConverter.convertOrderToOrderDto(order);
    }

    public Collection<Order> getAll() {
        return orderRepository.findAll();
    }

    public Order addOrder(Order order) {
        order.setDateAdded(LocalDateTime.now(ZoneId.of("Europe/Moscow")));
        return orderRepository.save(order);
    }

    public Optional<Order> getOrderByMonitorCode(String monitorCode) {
        return orderRepository.findByMonitorCode(monitorCode);
    }

    public Order addOrderDto(OrderDto orderDto) throws NoEntityException {
        Optional<Order> orderWithMonitorCode = getOrderByMonitorCode(orderDto.getMonitorCode());
        Order order = new Order();
        order.setDateTimeOrderedOn(orderDto.getOrderedOn());
        order.setMonitorCode(orderDto.getMonitorCode());
        try {
            order.setStatus(Status.valueOf(orderDto.getStatus()));
        } catch (Exception ex) {
            log.warn("Cannot parse Status value -> Status = created for " + orderDto.getMonitorCode());
            order.setStatus(Status.CREATED);
        }
        order.setBuilding(buildingService.getBuildingByName(orderDto.getBuildingName()));
        order.setProducts(getProductListById(orderDto.getProductIds()));
        order.setUser(getUserFromRepository(orderDto.getUserId()));
        orderWithMonitorCode.ifPresent(value -> {
            log.warn("Attemption!!! Rewriting order with new!!! " +
                            "Check previous {} code with [{}] products on {} for {} ({})",
                    orderWithMonitorCode.get().getMonitorCode(),
                    orderWithMonitorCode.get().getProducts(),
                    orderWithMonitorCode.get().getDateTimeOrderedOn(),
                    orderWithMonitorCode.get().getUser().getSurname(), orderWithMonitorCode.get().getUser().getId());
            order.setId(value.getId());
        });
        return orderRepository.save(order);
    }

    public Order changeStatus(Long id, Status status) throws NoEntityException {
        Order order = getOrder(id);
        order.setStatus(status);
        return addOrder(order);
    }

    public OrderDto changeStatusString(Long id, String statusAsString) throws NoEntityException {
        Status status;
        try{
            status = Status.valueOf(statusAsString);
        } catch (Exception ex) {
            log.warn("Cannot parse \"{}\" Status value -> Status = created", statusAsString);
            status = Status.CREATED;
        }
        Order order = getOrder(id);
        order.setStatus(status);
        return OrderConverter.convertOrderToOrderDto(addOrder(order));
    }

    public Integer getNumberOfPages() {
        int partial = 0;
        int totalFields = getAll().size();
        if (totalFields % pagesSize > 0)
            partial += 1;
        return (totalFields/pagesSize) + partial;
    }

    private List<Product> getProductListById(List<Long> productIds) throws NoEntityException {
        List<Product> products = new ArrayList<>();
        for (Long productId : productIds) {
            products.add(productService.getProduct(productId));
        }
        return products;
    }

    private User getUserFromRepository(Long id) throws NoEntityException {
        return userService.getUser(id);
    }
}
