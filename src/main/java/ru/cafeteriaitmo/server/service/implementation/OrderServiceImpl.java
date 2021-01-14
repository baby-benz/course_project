package ru.cafeteriaitmo.server.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    private final BuildingService buildingService;
    private  final ProductService productService;

    public Page<OrderDto> getOrderPage(Long pageNumber) {
        if (pageNumber < 0L) return null;
        Pageable pageable = PageRequest.of(pageNumber.intValue(), 5);
        Page<Order> orderPage = orderRepository.findAll(pageable);
        return changePageToDtoPage(orderPage);
    }

    public Order getOrder(Long orderId) throws NoEntityException {
        return orderRepository.findById(orderId).orElseThrow(() ->
                new NoEntityException(Order.class.getSimpleName().toLowerCase(), orderId));
    }

    public OrderDto getOrderDto(Long orderId) throws NoEntityException {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new NoEntityException(Order.class.getSimpleName().toLowerCase(), orderId));
        return convertOrderToOrderDto(order);
    }

    public Collection<Order> getAll() {
        return orderRepository.findAll();
    }

    public Order addOrder(Order order) {
        order.setDateAdded(LocalDateTime.now(ZoneId.of("Europe/Moscow")));
        return orderRepository.save(order);
    }

    public Order addOrderDto(OrderDto orderDto) throws NoEntityException {
        Order order = new Order();
        order.setDateTimeOrderedOn(orderDto.getOrderedOn());
        order.setMonitorCode(orderDto.getMonitorCode());
        try {
            order.setStatus(Status.valueOf(orderDto.getStatus()));
        } catch (Exception ex) {
            log.warn("Cannot parse Status value -> Status = created for " + orderDto.getMonitorCode());
            order.setStatus(Status.Created);
        }
        order.setBuilding(buildingService.getBuildingByName(orderDto.getBuildingName()));
        order.setProducts(getProductListById(orderDto.getProductIds()));
        order.setUser(getUserFromRepository(orderDto.getUserId()));
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
            status = Status.Created;
        }
        Order order = getOrder(id);
        order.setStatus(status);
        return orderToDto(addOrder(order));
}

    private List<Product> getProductListById(List<Long> productIds) throws NoEntityException {
        List<Product> products = new ArrayList<>();
        for (Long productId : productIds) {
            products.add(productService.getProduct(productId));
        }
        return products;
    }

    private User getUserFromRepository(Long id) throws NoEntityException {
        return userRepository.findById(id).orElseThrow(() ->
                new NoEntityException(User.class.getSimpleName().toLowerCase(), id));
    }

    private OrderDto orderToDto(Order order) {
        ArrayList<Long> productIds = new ArrayList<>();
        List<Product> products = (ArrayList<Product>) order.getProducts();
        for (Product product : products) {
            productIds.add(product.getId());
        }

        OrderDto dto = new OrderDto();
        dto.setStatus(order.getStatus().toString());
        dto.setOrderedOn(order.getDateTimeOrderedOn());
        dto.setBuildingName(order.getBuilding().getName());
        dto.setMonitorCode(order.getMonitorCode());
        dto.setProductIds(productIds);
        dto.setUserId(order.getUser().getId());
        return dto;
    }

    public Page<OrderDto> changePageToDtoPage(Page<Order> orderPage) {
        log.info("convert {} orders from page to dto", orderPage.getSize());

        Page<OrderDto> orderDtoPage;
        List<OrderDto> orderDtos = new ArrayList<>();
        List<Order> orders = orderPage.toList();
        for (Order order : orders) {
            orderDtos.add(convertOrderToOrderDto(order));
        }
        orderDtoPage = new PageImpl<>(orderDtos);
        return orderDtoPage;
    }

    private OrderDto convertOrderToOrderDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderedOn(order.getDateTimeOrderedOn());
        orderDto.setMonitorCode(order.getMonitorCode());
        orderDto.setBuildingName(order.getBuilding().getName());
        orderDto.setStatus(order.getStatus().toString());
        orderDto.setUserId(order.getUser().getId());

        Collection<Product> products = order.getProducts();
        ArrayList<Long> productsIds = new ArrayList<>();
        for (Product product : products) {
            productsIds.add(product.getId());
        }
        orderDto.setProductIds(productsIds);

        return orderDto;
    }
}
