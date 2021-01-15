package ru.cafeteriaitmo.server.controller.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.cafeteriaitmo.server.controller.exception.NoEntityException;
import ru.cafeteriaitmo.server.dto.ProductDto;
import ru.cafeteriaitmo.server.service.ProductService;
import ru.cafeteriaitmo.server.service.ProductTypeService;

@Slf4j
@RestController
@RequestMapping("/api/product/type")
@RequiredArgsConstructor
public class ProductTypeController {
    private final ProductTypeService productTypeService;

    @Value("${cafeteria.api.pages.size}")
    private Integer pagesSize;

    @GetMapping("/breakfast/{page}")
    public Page<ProductDto> getBreakfastProductPage(@PathVariable Long page) {
        log.info("Get requesting for {} page of breakfast", page);
        return productTypeService.getBreakfastProductPage(page);
    }

    @GetMapping("/starter/{page}")
    public Page<ProductDto> getStarterProductPage(@PathVariable Long page) {
        log.info("Get requesting for {} page of first", page);
        return productTypeService.getStarterProductPage(page);
    }

    @GetMapping("/second/{page}")
    public Page<ProductDto> getProductPage(@PathVariable Long page) {
        log.info("Get requesting for {} page of second", page);
        return productTypeService.getSecondProductPage(page);
    }

    @GetMapping("/drinking/{page}")
    public Page<ProductDto> getDrinkingProductPage(@PathVariable Long page) {
        log.info("Get requesting for {} page of second", page);
        return productTypeService.getDrinkingProductPage(page);
    }
}
