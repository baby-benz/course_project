package ru.cafeteriaitmo.server.filler.database;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.cafeteriaitmo.server.domain.entity.Building;
import ru.cafeteriaitmo.server.domain.entity.Image;
import ru.cafeteriaitmo.server.domain.entity.Product;
import ru.cafeteriaitmo.server.service.BuildingService;
import ru.cafeteriaitmo.server.service.ProductService;

import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@Slf4j
@Component
@RequiredArgsConstructor
public class Filler {
    private final ProductService productService;
    private final BuildingService buildingService;
    Image image;
    Product product;
    Building building;

    @Autowired
    private void createDailyMenuExample() {
        image = new Image();
        product = new Product();
        building = new Building();
        createImage();
        createBuidlings();
        createProducts();
        setDependencies();
        saveStock();
        showStock();
    }

    private void createImage() {
        Blob imageBytes = toByteArray("/images/item_1.jpg");
        this.image.setImage(imageBytes);
    }

    private void createBuidlings() {
        String address = "Кронверкский пр., 49, Санкт-Петербург";
        String name = "Кронверкский";
        building = Building.builder().address(address).name(name).build();
    }

    private void createProducts() {
        Boolean available = true;
        String description = "Сочная картошка поджаренная на растительном масле с оливками и " +
                "с яблочным соусом в мундире из отборных плодов картофеля. " +
                "Очень привлекательное блюдо со специфическим запахом детства и студенчества. " +
                "Не спеши - попробуй текст много текста, тестовая версия описания " +
                "ван лав)))))) Вкусно, попробуйте, не пожалеете, отвечаю. Всего за 99 рублей и немного монет";
        String name = "Жаренный картофель соломкой";
        Float price = 99.99F;
        String type = "Гарнир";
        product = Product.builder()
                .available(available)
                .description(description)
                .name(name)
                .price(price)
                .type(type)
                .build();
    }

    private void setDependencies() {
        product.setImage(image);

        Collection<Product> productInStock = new ArrayList<>();
        productInStock.add(product);
        building.setProducts(productInStock);
    }

    private void saveStock() {
        buildingService.add(building);
    }

    private void showStock() {
        System.out.println("_____products____");
        System.out.println(Arrays.toString(productService.getAll().toArray()));

        System.out.println("______buildings______");
        System.out.println(buildingService.getAll());
    }

    private Blob toByteArray(String path) {
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(getClass().getResource(path));
        } catch (IOException e) {
            log.error("Cannot read image!!!");
        }
        if (bufferedImage == null) {
            log.warn("meh, there is something gonna wrong.");
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "jpg", baos);
        } catch (IOException e) {
            log.error("meh image io");
        }
        Blob blobImage = null;
        try {
            blobImage = new SerialBlob(baos.toByteArray());
        } catch (SQLException throwables) {
            log.error("meh blob from array");
        }

        return blobImage;
    }
}
