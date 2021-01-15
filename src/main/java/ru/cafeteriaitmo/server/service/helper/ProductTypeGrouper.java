package ru.cafeteriaitmo.server.service.helper;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.cafeteriaitmo.server.domain.entity.Product;
import ru.cafeteriaitmo.server.domain.enums.Type;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@UtilityClass
public class ProductTypeGrouper {
    public List<Product> groupBreakfastProduct(List<Product> productList) {
        List<Product> breakfastProduct = new ArrayList<>();
        productList.forEach( product -> {
            if (product.getType().equals(Type.BREAKFAST.toString()))
                breakfastProduct.add(product);
        });
        return breakfastProduct;
    }

    public List<Product> groupStarterProduct(List<Product> productList) {
        List<Product> starterProduct = new ArrayList<>();
        productList.forEach( product -> {
            if (product.getType().equals(Type.STARTER.toString()))
                starterProduct.add(product);
        });
        return starterProduct;
    }

    public List<Product> groupSecondProduct(List<Product> productList) {
        List<Product> secondProduct = new ArrayList<>();
        productList.forEach( product -> {
            if (product.getType().equals(Type.SECOND.toString()))
                secondProduct.add(product);
        });
        return secondProduct;
    }

    public List<Product> groupDrinkingProduct(List<Product> productList) {
        List<Product> drinkingProduct = new ArrayList<>();
        productList.forEach( product -> {
            if (product.getType().equals(Type.DRINKING.toString()))
                drinkingProduct.add(product);
        });
        return drinkingProduct;
    }
}
