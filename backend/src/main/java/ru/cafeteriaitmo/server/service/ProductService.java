package ru.cafeteriaitmo.server.service;

import org.springframework.data.domain.Page;
import ru.cafeteriaitmo.server.controller.exception.NoEntityException;
import ru.cafeteriaitmo.server.domain.entity.Product;
import ru.cafeteriaitmo.server.dto.ProductDto;

import java.util.List;

public interface ProductService {
    Page<ProductDto> getProductDtoPageFromBuilding(String building, Integer page) throws NoEntityException;
    Page<ProductDto> getProductPage(Long page);

    Product getProduct(Long id) throws NoEntityException;
    ProductDto getProductDto(Long id) throws NoEntityException;
    List<Product> getAll();
    Integer getNumberOfPages();
    Product changeAvailable(Long id, Boolean available) throws NoEntityException;

    Product addProduct(Product product);
    Product addProductFromDto(ProductDto productDto) throws NoEntityException;
}
