package ru.cafeteriaitmo.server.service;

import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import ru.cafeteriaitmo.server.controller.exception.NoEntityException;
import ru.cafeteriaitmo.server.domain.entity.Product;

public interface ProductService {
    Page<Product> getPageFromBuilding (String building, Integer page) throws NoEntityException;
    Page<Product> getProductPage(Long page);
    Product getProduct(Long id) throws NoEntityException;
    Product addProduct(JSONObject productJson);
}
