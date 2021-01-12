package ru.cafeteriaitmo.server.service.implementation;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.cafeteriaitmo.server.controller.exception.NoEntityException;
import ru.cafeteriaitmo.server.domain.entity.Building;
import ru.cafeteriaitmo.server.domain.entity.Product;
import ru.cafeteriaitmo.server.repository.ProductRepository;
import ru.cafeteriaitmo.server.service.BuildingService;
import ru.cafeteriaitmo.server.service.ProductService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final BuildingService buildingService;

    public Page<Product> getProductPage(Long pageNumber) {
        if (pageNumber < 0L) return null;
        Pageable pageable = PageRequest.of(pageNumber.intValue(), 5);
        return productRepository.findAll(pageable);
    }

    public Product getProduct(Long id) throws NoEntityException {
        return productRepository.findById(id).orElseThrow(() ->
                new NoEntityException(Product.class.getSimpleName().toLowerCase(), id));
    }

    public Page<Product> getPageFromBuilding(String buildingName, Integer page) throws NoEntityException {
        Building building = buildingService.getBuildingByName(buildingName);
        Pageable pageable = PageRequest.of(page, 15);
        return productRepository.findAllProductsByBuilding(building, pageable);
    }

    public Product addProduct(JSONObject productJson) {
        return null;
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getAll(){
        return productRepository.findAll();
    }
}
