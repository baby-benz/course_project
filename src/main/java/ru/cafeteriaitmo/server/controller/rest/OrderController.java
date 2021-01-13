package ru.cafeteriaitmo.server.controller.rest;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.cafeteriaitmo.server.controller.exception.NoEntityException;
import ru.cafeteriaitmo.server.domain.entity.Order;
import ru.cafeteriaitmo.server.domain.enums.Status;
import ru.cafeteriaitmo.server.service.OrderService;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/{page}")
    public @ResponseBody Page<Order> getOrderPage(@PathVariable long page) {
        return orderService.getOrderPage(page);
    }

    @GetMapping
    public Order getOrder(@RequestParam long orderId) throws NoEntityException {
        return orderService.getOrder(orderId);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping
    public Order doOrder(Order order) {
        return orderService.addOrder(order);
    }

    @PatchMapping("{id}")
    public Order changeStatus(@RequestBody JSONObject statusJson) {
        String statusName = (String) statusJson.get("status");
        Status status =  Status.Created;
        return null;
    }

}
