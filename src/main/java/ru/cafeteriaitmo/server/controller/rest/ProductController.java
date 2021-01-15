package ru.cafeteriaitmo.server.controller.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.cafeteriaitmo.server.controller.exception.NoEntityException;
import ru.cafeteriaitmo.server.domain.entity.Product;
import ru.cafeteriaitmo.server.dto.ProductDto;
import ru.cafeteriaitmo.server.service.ProductService;

@Slf4j
@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @Value("${cafeteria.api.pages.size}")
    private Integer pagesSize;

    @GetMapping
    public ProductDto getProduct(@RequestParam Long id) throws NoEntityException {
        log.info("Get request with product {} id", id);
        return productService.getProductDto(id);
    }

    @GetMapping("/{page}")
    public Page<ProductDto> getProductPage(@PathVariable Long page) {
        log.info("Get requesting {} product page", page);
        return productService.getProductPage(page);
    }

    //TODO: change path for BestPractices
    @GetMapping("/{page}/building")
    public Page<ProductDto> getProductPageFromBuilding(@PathVariable Long page, @RequestParam String name) throws NoEntityException {
        log.info("Get request for {} product page from {} building", page, name);
        return productService.getProductDtoPageFromBuilding(name, page.intValue());
    }

    @PostMapping
    public String addProduct(ProductDto productDto) throws NoEntityException {
        log.info("Post request to add product");
        productService.addProductFromDto(productDto);
        return "Product added into server";
    }

    @GetMapping("/pages")
    public Integer getNumberOfPages() {
        log.info("get request to check total orders pages");
        Integer totalPages = productService.getNumberOfPages();
        log.info("total: {} pages", totalPages);
        return totalPages;
    }
}
