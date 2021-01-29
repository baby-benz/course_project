package ru.cafeteriaitmo.server.controller.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import javax.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/{page}")
    public Page<OrderDto> getOrderPage(@PathVariable Long page) {
        log.info("Get request for {} product page", page);
        return orderService.getOrderPage(page);
    }

    @GetMapping("/monitor")
    public OrderDto getOrderDtoByMonitorCode(@RequestBody String monitorCode) throws NoEntityException {
        log.info("Get request for {} monitor code", monitorCode);
        return orderService.getOrderByMonitorCode(monitorCode);
    }

    @Deprecated
    @GetMapping
    public OrderDto getOrderDto(@RequestParam Long orderId) throws NoEntityException {
        log.info("Get request for {} order id", orderId);
        return orderService.getOrderDto(orderId);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping
    public void createOrder(@RequestBody OrderDto order, HttpServletRequest requestContext) throws NoEntityException {
        log.info("Post request for adding order by {}:{}", requestContext.getRemoteAddr(), requestContext.getRemotePort());
        orderService.addOrderDto(order);
    }

    @PatchMapping("{id}")
    public Order changeStatus(@PathVariable Long id, @RequestBody Status status) throws NoEntityException {
        log.info("Patch request to change status of {} order to {}", id, status.toString());
        return orderService.changeStatus(id, status);
        }

    @PatchMapping("{id}/status")
    public void changeStatusByUrlStringParam(@PathVariable Long id, @RequestParam String status) throws NoEntityException {
        if (status.contains(",")) {
            if (status.contains(("\"")))
                status = status.substring(1, status.indexOf(","));
            else
                status = status.substring(0, status.indexOf(","));
        }
        log.info("Patch request to change status of {} order to \"{}\"", id, status);
        orderService.changeStatusString(id, status.toUpperCase());
    }

    @GetMapping("/pages")
    public Integer getNumberOfPages() {
        log.info("Get request to check total orders pages");
        Integer totalPages = orderService.getNumberOfPages();
        log.info("total: {} pages", totalPages);
        return totalPages;
    }
}
