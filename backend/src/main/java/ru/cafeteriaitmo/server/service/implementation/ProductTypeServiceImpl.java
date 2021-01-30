package ru.cafeteriaitmo.server.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.cafeteriaitmo.server.domain.entity.Product;
import ru.cafeteriaitmo.server.domain.enums.Type;
import ru.cafeteriaitmo.server.dto.ProductDto;
import ru.cafeteriaitmo.server.repository.ProductRepository;
import ru.cafeteriaitmo.server.service.ProductTypeService;
import ru.cafeteriaitmo.server.service.helper.ProductConverter;
import ru.cafeteriaitmo.server.service.helper.ProductTypeGrouper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductTypeServiceImpl implements ProductTypeService {
    private final ProductRepository productRepository;

    @Value("${cafeteria.api.pages.size}")
    private Integer pagesSize;

    public Page<ProductDto> getBreakfastProductPage(Long page) {
        Pageable pageable = PageRequest.of(page.intValue(), pagesSize);
        Page<Product> breakfastPage = productRepository.findAllProductsByType(Type.BREAKFAST.toString(), pageable);
        return ProductConverter.changePageToDtoPage(breakfastPage);
    }

    public Page<ProductDto> getStarterProductPage(Long page) {
        Pageable pageable = PageRequest.of(page.intValue(), pagesSize);
        Page<Product> breakfastPage = productRepository.findAllProductsByType(Type.STARTER.toString(), pageable);
        return ProductConverter.changePageToDtoPage(breakfastPage);
    }

    public Page<ProductDto> getSecondProductPage(Long page) {
        Pageable pageable = PageRequest.of(page.intValue(), pagesSize);
        Page<Product> breakfastPage = productRepository.findAllProductsByType(Type.SECOND.toString(), pageable);
        return ProductConverter.changePageToDtoPage(breakfastPage);
    }

    public Page<ProductDto> getDrinkingProductPage(Long page) {
        Pageable pageable = PageRequest.of(page.intValue(), pagesSize);
        Page<Product> breakfastPage = productRepository.findAllProductsByType(Type.DRINKING.toString(), pageable);
        return ProductConverter.changePageToDtoPage(breakfastPage);
    }
}
