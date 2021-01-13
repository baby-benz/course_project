package ru.cafeteriaitmo.server.service;

import org.springframework.data.domain.Page;
import ru.cafeteriaitmo.server.controller.exception.NoEntityException;
import ru.cafeteriaitmo.server.domain.entity.Product;

import java.util.List;

public interface ProductService {
    Page<Product> getPageFromBuilding (String building, Integer page) throws NoEntityException;
    Page<Product> getProductPage(Long page);
    Product getProduct(Long id) throws NoEntityException;
    List<Product> getAll();
    Product addProduct(Product product);
}
