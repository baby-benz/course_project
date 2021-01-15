package ru.cafeteriaitmo.server.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.cafeteriaitmo.server.controller.exception.NoEntityException;
import ru.cafeteriaitmo.server.domain.entity.Building;
import ru.cafeteriaitmo.server.domain.entity.Image;
import ru.cafeteriaitmo.server.domain.entity.Order;
import ru.cafeteriaitmo.server.domain.entity.Product;
import ru.cafeteriaitmo.server.dto.ProductDto;
import ru.cafeteriaitmo.server.repository.ProductRepository;
import ru.cafeteriaitmo.server.service.BuildingService;
import ru.cafeteriaitmo.server.service.ProductService;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final BuildingService buildingService;

    @Value("${cafeteria.api.pages.size}")
    private Integer pagesSize;

    public Page<ProductDto> getProductPage(Long pageNumber) {
        if (pageNumber < 0L) return null;
        Pageable pageable = PageRequest.of(pageNumber.intValue(), pagesSize);
        Page<Product> productPage = productRepository.findAll(pageable);
        return changePageToDtoPage(productPage);
    }

    public Product getProduct(Long id) throws NoEntityException {
        return productRepository.findById(id).orElseThrow(() ->
                new NoEntityException(Product.class.getSimpleName().toLowerCase(), id));
    }

    public ProductDto getProductDto(Long id) throws NoEntityException {
        ProductDto productDto;
        Product product = productRepository.findById(id).orElseThrow(() ->
                new NoEntityException(Product.class.getSimpleName().toLowerCase(), id));
        productDto = convertProductToProductDto(product);
        return productDto;
    }

    public Page<ProductDto> getProductDtoPageFromBuilding(String buildingName, Integer page) throws NoEntityException {
        Building building = buildingService.getBuildingByName(buildingName);
        Pageable pageable = PageRequest.of(page, pagesSize);
        Page<Product> productPage = productRepository.findAllProductsByBuilding(building, pageable);
        return changePageToDtoPage(productPage);
    }

    public Integer getNumberOfPages() {
        int partial = 0;
        int totalFields = getAll().size();
        if (totalFields % pagesSize > 0)
            partial += 1;
        return (totalFields/pagesSize) + partial;
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public Product addProductFromDto(ProductDto productDto) throws NoEntityException {
        return productRepository.save(dtoToProduct(productDto));
    }

    public List<Product> getAll(){
        return productRepository.findAll();
    }

    public Product dtoToProduct(ProductDto productDto) throws NoEntityException {
        Building building = buildingService.getBuildingByName(productDto.getNameBuilding());
        Image image = new Image();
        image.setImage(Base64.getDecoder().decode(productDto.getImage()));

        Product product = Product.builder()
                .available(productDto.getAvailable())
                .building(building)
                .image(image)
                .description(productDto.getDescription())
                .name(productDto.getName())
                .price(productDto.getPrice())
                .type(productDto.getType())
                .build();
        return product;
    }

    private Page<ProductDto> changePageToDtoPage(Page<Product> productPage) {
        log.info("convert {} products from page to dto", productPage.toList().size());

        Page<ProductDto> productDtoPage;
        List<ProductDto> productDtos = new ArrayList<>();
        List<Product> products = productPage.toList();
        for (Product product : products) {
            productDtos.add(convertProductToProductDto(product));
        }
        productDtoPage = new PageImpl<>(productDtos);
        return productDtoPage;
    }

    private ProductDto convertProductToProductDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setAvailable(product.getAvailable());
        productDto.setDescription(product.getDescription());

        byte[] imageByte = product.getImage().getImage();

        productDto.setImage(Base64.getEncoder().encodeToString(imageByte));
        productDto.setNameBuilding(product.getBuilding().getName());
        productDto.setPrice(product.getPrice());
        productDto.setType(product.getType());
        return productDto;
    }
}
