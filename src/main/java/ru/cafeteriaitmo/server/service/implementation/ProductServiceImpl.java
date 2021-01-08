package ru.cafeteriaitmo.server.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.cafeteriaitmo.server.domain.entity.Building;
import ru.cafeteriaitmo.server.domain.entity.Product;
import ru.cafeteriaitmo.server.repository.ProductRepository;
import ru.cafeteriaitmo.server.service.ProductService;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public Page<Product> getPageFromBuilding(Building building, Integer page) {
        Pageable pageable = PageRequest.of(page, 15);
        return productRepository.findAllProductsByBuilding(building, pageable);
    }
}
