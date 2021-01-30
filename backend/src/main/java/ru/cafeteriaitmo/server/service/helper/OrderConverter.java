package ru.cafeteriaitmo.server.service.helper;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.cafeteriaitmo.server.domain.entity.Order;
import ru.cafeteriaitmo.server.domain.entity.Product;
import ru.cafeteriaitmo.server.dto.OrderDto;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@UtilityClass
public class OrderConverter {
    public Page<OrderDto> changePageToDtoPage(Page<Order> orderPage) {
        log.info("convert {} orders from page to dto", orderPage.toList().size());

        Page<OrderDto> orderDtoPage;
        List<OrderDto> orderDtos = new ArrayList<>();
        List<Order> orders = orderPage.toList();
        for (Order order : orders) {
            orderDtos.add(convertOrderToOrderDto(order));
        }
        orderDtoPage = new PageImpl<>(orderDtos);
        return orderDtoPage;
    }

    public OrderDto convertOrderToOrderDto(Order order) {
        ArrayList<Long> productIds = new ArrayList<>();
        List<Product> products = (List<Product>) order.getProducts();
        for (Product product : products) {
            productIds.add(product.getId());
        }

        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setStatus(order.getStatus().toString());
        dto.setOrderedOn(order.getDateTimeOrderedOn());
        dto.setBuildingName(order.getBuilding().getName());
        dto.setMonitorCode(order.getMonitorCode());
        dto.setProductIds(productIds);
        dto.setUserId(order.getUser().getId());
        return dto;
    }
}
