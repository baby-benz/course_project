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

    public OrderDto getOrderDto(Long orderId) throws NoEntityException {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new NoEntityException(Order.class.getSimpleName().toLowerCase(), orderId.toString()));
        return OrderConverter.convertOrderToOrderDto(order);
    }

    public Collection<Order> getAll() {
        return orderRepository.findAll();
    }

    public Order addOrder(Order order) {
        order.setDateAdded(LocalDateTime.now(ZoneId.of("Europe/Moscow")));
        return orderRepository.save(order);
    }

    public OrderDto getOrderByMonitorCode(String monitorCode) throws NoEntityException {
        Order order =  orderRepository.findByMonitorCode(monitorCode).orElseThrow(() ->
                new NoEntityException(Order.class.getSimpleName().toLowerCase(), monitorCode));
        return OrderConverter.convertOrderToOrderDto(order);
    }

    public Order addOrderDto(OrderDto orderDto) throws NoEntityException {
//        Optional<Order> orderWithMonitorCode = orderRepository.findByMonitorCode(orderDto.getMonitorCode());
        Order order = new Order();
        order.setDateTimeOrderedOn(LocalDateTime.parse(orderDto.getOrderedOn()));
        order.setMonitorCode(orderDto.getMonitorCode());
        try {
            order.setStatus(Status.valueOf(orderDto.getStatus().toUpperCase()));
        } catch (Exception ex) {
            log.warn("Cannot parse Status value {} -> CREATED for " + orderDto.getMonitorCode(), orderDto.getStatus());
            order.setStatus(Status.CREATED);
        }
        order.setBuilding(buildingService.getBuildingByName(orderDto.getBuildingName()));
        order.setProducts(getProductListById(orderDto.getProductIds()));
        try{
            if (userService.getUserByPersonalNumber(orderDto.getUserPersonalNumber()) != null)
                order.setUser(getUserFromRepository(orderDto.getUserPersonalNumber()));
        } catch (NoEntityException ex) {
            User user = new User();
            //TODO: Костыль
            user.setName(orderDto.getMonitorCode().substring(0, orderDto.getMonitorCode().indexOf(" ")));
            user.setSurname(orderDto.getMonitorCode().substring(orderDto.getMonitorCode().indexOf(" "),
                    orderDto.getMonitorCode().indexOf("(")-1));
            user.setPersonalNumber(orderDto.getUserPersonalNumber());
            order.setUser(user);
            log.warn("User not found. Created new: {} {} number:{}", user.getName(),
                    user.getSurname(),
                    user.getPersonalNumber());
        }
//        orderWithMonitorCode.ifPresent(value -> {
//            log.warn("Attemption!!! Rewriting order with new!!! " +
//                            "Check previous {} code with [{}] products on {} for {} ({})",
//                    orderWithMonitorCode.get().getMonitorCode(),
//                    orderWithMonitorCode.get().getProducts(),
//                    orderWithMonitorCode.get().getDateTimeOrderedOn(),
//                    orderWithMonitorCode.get().getUser().getSurname(), orderWithMonitorCode.get().getUser().getId());
//            order.setId(value.getId());
//        });
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

    private User getUserFromRepository(String personalNumber) throws NoEntityException {
        return userService.getUserByPersonalNumber(personalNumber);
    }

    @Deprecated
    public Order getOrder(Long orderId) throws NoEntityException {
        return orderRepository.findById(orderId).orElseThrow(() ->
                new NoEntityException(Order.class.getSimpleName().toLowerCase(), orderId.toString()));
    }
}
