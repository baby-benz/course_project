package ru.cafeteriaitmo.server.controller.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.cafeteriaitmo.server.domain.entity.Order;
import ru.cafeteriaitmo.server.domain.entity.Product;

import java.util.List;

@RestController("/api/order")
public class OrderController {

    @GetMapping
    public @ResponseBody Order getOrder() {
        return null;
    }

    @PostMapping
    public void doOrder(List<Product> products) {

    }

}
