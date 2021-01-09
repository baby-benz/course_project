package ru.cafeteriaitmo.server.service.implementation;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.cafeteriaitmo.server.controller.exception.NoEntityException;
import ru.cafeteriaitmo.server.domain.entity.Building;
import ru.cafeteriaitmo.server.domain.entity.Order;
import ru.cafeteriaitmo.server.domain.entity.Product;
import ru.cafeteriaitmo.server.domain.entity.User;
import ru.cafeteriaitmo.server.domain.enums.Status;
import ru.cafeteriaitmo.server.repository.OrderRepository;
import ru.cafeteriaitmo.server.repository.UserRepository;
import ru.cafeteriaitmo.server.service.BuildingService;
import ru.cafeteriaitmo.server.service.OrderService;
import ru.cafeteriaitmo.server.service.ProductService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    private final BuildingService buildingService;
    private  final ProductService productService;

    public Page<Order> getOrderPage(Long pageNumber) {
        if (pageNumber < 0L) return null;
        Pageable pageable = PageRequest.of(pageNumber.intValue(), 5);
        return orderRepository.findAll(pageable);
    }

    public Order getOrder(Long orderId) throws NoEntityException {
        return orderRepository.findById(orderId).orElseThrow(() ->
                new NoEntityException(Order.class.getSimpleName().toLowerCase(), orderId));
    }

    public Collection<Order> getAll() {
        return orderRepository.findAll();
    }

    /**
     *
     * @param valuesToCreateOrder (example) -> JSONObject
     *        products : [id : 1, id : 2, id : 5],
     *        user_id : Long id,
     *        building_name : String name
     *        time : timeOrderedOn
     *
     */
    public Order createOrder(JSONObject valuesToCreateOrder) throws NoEntityException {
        Order order = parseJsonOrderToCreate(valuesToCreateOrder);
        return orderRepository.save(order);
    }

    private Order parseJsonOrderToCreate(JSONObject values) throws NoEntityException {
        User user;
        LocalDateTime dateAdded;
        Collection<Product> products = null;
        LocalDateTime dateTimeOrderedOn;
        Building building;

        Long userId = (Long) values.get("user_id");
        user = getUserWithId(userId);

        dateAdded = LocalDateTime.now(ZoneId.of("Europe/Moscow"));
        dateTimeOrderedOn = (LocalDateTime) values.get("time"); //TODO: а как передается LocalDateTime в json?
        building = getBuildingWithName((String) values.get("building_name"));
        products = parseProductsFromJson((JSONArray) values.get("products"));

        Order order = Order.builder()
                .user(user)
                .dateAdded(dateAdded)
                .productsOrdered(products)
                .dateTimeOrderedOn(dateTimeOrderedOn)
                .building(building)
                .status(Status.Created)
                .build();
        return null;
    }

    private User getUserWithId(Long id) throws NoEntityException {
        return userRepository.findById(id).orElseThrow( () ->
                new NoEntityException(User.class.getSimpleName().toLowerCase(), id));
    }

    private Building getBuildingWithName(String buildingName) throws NoEntityException {
        return buildingService.getBuildingByName(buildingName);
    }

    private Collection<Product> parseProductsFromJson(JSONArray productsJsonArray) throws NoEntityException {
        Collection<Product> products = new ArrayList<>();
        for (Object productId : productsJsonArray) {
            Map<String, Long> map = (Map) productId; //TODO: передавать Long
            products.add(productService.getProduct(map.get("id")));
        }
        return null;
    }
}
