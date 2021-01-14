package ru.cafeteriaitmo.server.controller.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.cafeteriaitmo.server.controller.exception.NoEntityException;
import ru.cafeteriaitmo.server.domain.entity.Order;
import ru.cafeteriaitmo.server.domain.enums.Status;
import ru.cafeteriaitmo.server.dto.OrderDto;
import ru.cafeteriaitmo.server.service.OrderService;

@Slf4j
@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @Value("${cafeteria.api.pages.size}")
    private Integer pagesSize;

    @GetMapping("/{page}")
    public Page<OrderDto> getOrderPage(@PathVariable long page) {
        log.info("Get request for {} product page", page);
        return orderService.getOrderPage(page);
    }

    @GetMapping
    public OrderDto getOrderDto(@RequestParam long orderId) throws NoEntityException {
        log.info("Get request for {} order id", orderId);
        return orderService.getOrderDto(orderId);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping
    public Order createOrder(OrderDto order) throws NoEntityException {
        log.info("Post request for adding order");
        return orderService.addOrderDto(order);
    }

    @PatchMapping("{id}")
    public Order changeStatus(@PathVariable Long id, Status status) throws NoEntityException {
        return orderService.changeStatus(id, status);
    }

    @PatchMapping("{id}/status")
    public void changeStatusString(@PathVariable Long id, @RequestParam String status) throws NoEntityException {
        orderService.changeStatusString(id, status);
    }

    //TODO: убрать в сервис (пока проверяю)
    @GetMapping("/pages")
    public Integer getNumberOfPages() {
        int partial = 0;
        int totalFields = orderService.getAll().size();
        if (totalFields % pagesSize > 0)
            partial += 1;
        return (totalFields/pagesSize) + partial;
    }
}
