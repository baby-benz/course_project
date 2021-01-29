package ru.cafeteriaitmo.server.service.helper;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.cafeteriaitmo.server.domain.entity.Product;
import ru.cafeteriaitmo.server.dto.ProductDto;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Slf4j
@UtilityClass
public class ProductConverter {
    public Page<ProductDto> changePageToDtoPage(Page<Product> productPage) {
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

    public ProductDto convertProductToProductDto(Product product) {
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
