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
import ru.cafeteriaitmo.server.controller.exception.ResponseException;
import ru.cafeteriaitmo.server.domain.entity.Order;
import ru.cafeteriaitmo.server.domain.enums.Status;
import ru.cafeteriaitmo.server.dto.OrderDto;
import ru.cafeteriaitmo.server.service.OrderService;

import java.util.Map;

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
    public Order changeStatusOrAvailable(@PathVariable Long id, Map<String, Object> is) throws NoEntityException {
        if (is.containsKey("status")) {
            Status status = Status.valueOf(is.get("status").toString());
            log.info("get request to change status of {} order to {}", id, status.toString());
            return orderService.changeStatus(id, status);
        }
        if (is.containsKey("available")) {
            Boolean available = (Boolean) is.get("available");
            return null;
        }
        return new Order();
    }

    @PatchMapping("{id}/status")
    public void changeStatusString(@PathVariable Long id, @RequestParam String status) throws NoEntityException {
        status = status.toUpperCase();
        log.info("get request to change status of {} order to \"{}\"", id, status);
        orderService.changeStatusString(id, status);
    }

    @PatchMapping("{id}/available")
    public void changeAvailableString(@PathVariable Long id, @RequestParam String available){
        log.info("get request to change status of {} order to \"{}\"", id, available);
    }

    //TODO: убрать в сервис (пока проверяю)
    @GetMapping("/pages")
    public Integer getNumberOfPages() {
        log.info("get request to check total orders pages");
        Integer totalPages = orderService.getNumberOfPages();
        log.info("total: {} pages", totalPages);
        return totalPages;
    }
}
