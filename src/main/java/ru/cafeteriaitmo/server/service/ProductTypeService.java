package ru.cafeteriaitmo.server.service;

import org.springframework.data.domain.Page;
import ru.cafeteriaitmo.server.dto.ProductDto;

public interface ProductTypeService {
    Page<ProductDto> getBreakfastProductPage(Long page);
    Page<ProductDto> getStarterProductPage(Long page);
    Page<ProductDto> getSecondProductPage(Long page);
    Page<ProductDto> getDrinkingProductPage(Long page);
}
