package ru.cafeteriaitmo.server.filler.database;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.cafeteriaitmo.server.domain.entity.Building;
import ru.cafeteriaitmo.server.domain.entity.Order;
import ru.cafeteriaitmo.server.domain.entity.Product;
import ru.cafeteriaitmo.server.domain.entity.User;
import ru.cafeteriaitmo.server.domain.enums.Status;
import ru.cafeteriaitmo.server.service.BuildingService;
import ru.cafeteriaitmo.server.service.OrderService;
import ru.cafeteriaitmo.server.service.ProductService;
import ru.cafeteriaitmo.server.service.UserService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class OneOrderDataFiller {
    private final ProductService productService;
    private final BuildingService buildingService;
    private final OrderService orderService;

    public String createOneOrder() {
        List<Product> products = productService.getAll();
        List<Building> buildings = buildingService.getAll();

        Building building = buildings.get(1);
        List<Product> productsFromBuilding = new ArrayList<>();

        for (Product product : products) {
            if (product.getBuilding().getName().equals(building.getName()))
                productsFromBuilding.add(product);
        }

        List<Product> productsToOrder = productsFromBuilding.subList(0, productsFromBuilding.size()/2);
        User user = createUser();
        Order order = createOrder(building, productsToOrder, user);
        orderService.addOrder(order);
        System.out.println("OK");
        return "OK";
    }

    private Order createOrder(Building building, List<Product> products, User user) {
        Order order = new Order();

        order.setStatus(Status.Prepairing);
        order.setBuilding(building);
        order.setProducts(products);
        order.setDateTimeOrderedOn(LocalDateTime.now(ZoneId.of("Europe/Moscow")));
        order.setMonitorCode("Test adding");
        order.setDateAdded(LocalDateTime.now(ZoneId.of("Europe/Moscow")));
        order.setUser(user);

        return order;
    }

    private User createUser() {
        User user = new User();
        user.setSurname("Тестовый <" + Math.random() + "> ");
        user.setName("Пользователь");
        return user;
    }
}
