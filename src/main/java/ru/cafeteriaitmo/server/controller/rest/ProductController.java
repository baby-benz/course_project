package ru.cafeteriaitmo.server.controller.rest;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.cafeteriaitmo.server.controller.exception.NoEntityException;
import ru.cafeteriaitmo.server.domain.entity.Product;
import ru.cafeteriaitmo.server.dto.ProductDto;
import ru.cafeteriaitmo.server.service.ProductService;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/{page}")
    public Page<Product> getProductPage(@PathVariable Long page) {
        return productService.getProductPage(page);
    }

    //TODO: change path for BestPractices
    @GetMapping("/{page}/building")
    public Page<Product> getProductPageFromBuilding(@PathVariable Long page, @RequestParam String buildingName) throws NoEntityException {
        return productService.getPageFromBuilding(buildingName, page.intValue());
    }

    @PostMapping
    public Product addProduct(ProductDto productDto) {
        return null;
    }
}
