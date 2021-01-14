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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderDataFiller {
    private final BuildingService buildingService;
    private final OrderService orderService;
    private final UserService userService;
    private final ProductService productService;

    String names[] = {"Вася", "Коля", "Леха", "Стас", "Никита"};
    String surnames[] = {"Левый","БогПрограммирования","СетевойБог","БогЛид","БогСкриптов"};


    public void createUsersOrdering() {
        List<Building> buildings = buildingService.getAll();
        List<Product> menu = productService.getAll();
        List<Product> productsOrdering = new ArrayList<>();

        User user;
        Building building;

        for (int i=0; i<5; i++) {
            user = createUser(names[i], surnames[i]);
            building = buildings.get(i%3);

            for (int j=i*2; j<menu.size(); j++) {
                if (menu.get(j).getBuilding().getName().equals(building.getName()) && menu.get(j).getAvailable())
                    productsOrdering.add(menu.get(j));
                if (productsOrdering.size() >= 1) break;
            }

            user.setOrders(createOrder(building, productsOrdering, i));
            userService.addUser(user);
            productsOrdering.clear();
        }

        showOrders();
    }

    private User createUser(String name, String surname) {
        User user = new User();
        user.setName(name);
        user.setSurname(surname);
        return user;
    }

    private List<Order> createOrder(Building building, Collection<Product> products, Integer id_monitor) {
        Order order = new Order();
        order.setDateAdded(LocalDateTime.now(ZoneId.of("Europe/Moscow")));
        order.setBuilding(building);
        order.setDateTimeOrderedOn(LocalDateTime.now(ZoneId.of("Europe/Moscow")));
        order.setMonitorCode("Test 10" + id_monitor);
        order.setStatus(Status.Created);
        order.setProducts(products);
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        return orders;
    }

    private void showOrders() {
        System.out.println("______orders______");
        System.out.println(Arrays.toString(orderService.getAll().toArray()));
    }

}
