package ru.cafeteriaitmo.server.service;

import org.springframework.data.domain.Page;
import ru.cafeteriaitmo.server.domain.entity.Building;
import ru.cafeteriaitmo.server.domain.entity.Product;

public interface ProductService {
    Page<Product> getPageFromBuilding (Building building, Integer page);
}
