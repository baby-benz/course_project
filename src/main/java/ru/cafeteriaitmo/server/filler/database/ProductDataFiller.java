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
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductDataFiller {
    private final ProductService productService;
    private final BuildingService buildingService;

    String descr1 = "Сочная картошка поджаренная на растительном масле с оливками и " +
            "с яблочным соусом в мундире из отборных плодов картофеля. " +
            "Очень привлекательное блюдо со специфическим запахом детства и студенчества. " +
            "Не спеши - попробуй текст много текста, тестовая версия описания " +
            "ван лав)))))) Вкусно, попробуйте, не пожалеете, отвечаю. Всего за 99 рублей и немного монет";
    String descr2 = "Ммммм, салатик, свеженький";
    String descr3 = "Какой-то сок с лимоном, честно не пробовал и вам не советую";
    String descr4 = "Это вообще какой-то чупа чупс, чуитс";
    String descr5 = "Еееее, куриный супчик!!! А БУЛЬЙОН-то какой, сказка! во!";
    String descr6 = "Борщ. Всё как дома.";
    String descr7 = "Макарошки, с фаршиком, будешь?";
    String descr8 = "Греча или гречка? а может перловку?";
    String descr9 = "寿司か何？";
    String descr10 = "Какие-то беллые роллы или суши";
    String descr11 = "У нас сегодня пюрешка, с котлеткой";
    String descr12 = "Котлетки каждый день ням ням";
    String descr13 = "Суперские пирожки с картошкой";
    String descr14 = "Что-то из рязрядка пирожных";
    String descr15 = "Что-то там по пирожным, но сладкого не хочется? тогда попробуйте ежевику " +
            "или малину в обертке из под теста (уверяю вас, не сладкое) " +
            "(кому сладко - лайк)";
    String descr16 = "Хлеб всему дом";
    String descr17 = "Дарагой, падхади, вазьми шаверму. Шаурма от Кержа. Всё по 100";
    String descr18 = "Компот как дома, хорошо помогает в летний знойный день.";

    String name1 = "Жареный картофель";
    String name2 = "Салат Летний";
    String name3 = "Лимонный сок";
    String name4 = "Напиток газированыый Чупс";
    String name5 = "Суп куриный";
    String name6 = "Борщ";
    String name7 = "Макароны по нефлотски";
    String name8 = "Греча";
    String name9 = "Роллы дефолт";
    String name10 = "Суши премиум";
    String name11 = "Картофельное пюре";
    String name12 = "Котлета";
    String name13 = "Пирожки";
    String name14 = "Пирожное со сливками";
    String name15 = "Пирожное с ежевикой";
    String name16 = "Хлеб с отрубями";
    String name17 = "Шаверма";
    String name18 = "Компот";

    String type1 = "Первое";
    String type2 = "Салат";
    String type3 = "Напиток";
    String type4 = "Напиток";
    String type5 = "Первое";
    String type6 = "Первое";
    String type7 = "Второе";
    String type8 = "Второе";
    String type9 = "Закуска";
    String type10 = "Закуска";
    String type11 = "Второе";
    String type12 = "Гарнир";
    String type13 = "Закуска";
    String type14 = "Сладкое";
    String type15 = "Сладкое";
    String type16 = "Гарнир";
    String type17 = "Второе";
    String type18 = "Напиток";

    public void createDailyMenuExample() {
        List<Product> products = new ArrayList<>();
        createBuidlings();

        //TODO: тестово же ыъъы
        products.add(createProducts(true, descr1, name1, type1));
        products.add(createProducts(true, descr2, name2, type2));
        products.add(createProducts(true, descr3, name3, type3));
        products.add(createProducts(true, descr4, name4, type4));
        products.add(createProducts(true, descr5, name5, type5));
        products.add(createProducts(true, descr6, name6, type6));
        products.add(createProducts(false, descr7, name7, type7));
        products.add(createProducts(false, descr8, name8, type8));
        products.add(createProducts(true, descr9, name9, type9));
        products.add(createProducts(true, descr10, name10, type10));
        products.add(createProducts(true, descr11, name11, type11));
        products.add(createProducts(true, descr12, name12, type12));
        products.add(createProducts(true, descr13, name13, type13));
        products.add(createProducts(false, descr14, name14, type14));
        products.add(createProducts(true, descr15, name15, type15));
        products.add(createProducts(true, descr16, name16, type16));
        products.add(createProducts(true, descr17, name17, type17));
        products.add(createProducts(true, descr18, name18, type18));

        List<Building> buildings = buildingService.getAll();
        for (int i = 0; i < products.size(); i++) {
            setDependencies(products.get(i), createImage(i+1), buildings.get(i % 3));
        }

        showStock();
    }

    private Image createImage(Integer number) {
        byte[] imageBytes = toByteArray("/images/item_" + number + ".jpg");

        Image image = new Image();
        image.setImage(imageBytes);
        return image;
    }

    private void createBuidlings() {
        String address = "Кронверкский проспект, 49, Санкт-Петербург";
        String name = "Кронверкский";
        buildingService.addBuilding(Building.builder().address(address).name(name).build());

        String address2 = "улица Ломоносова, 9, Санкт-Петербург";
        String name2 = "Ломо";
        buildingService.addBuilding(Building.builder().address(address2).name(name2).build());

        String address1 = "пер. Гривцова, 14, Санкт-Петербург";
        String name1 = "Гривцова";
        buildingService.addBuilding(Building.builder().address(address1).name(name1).build());
    }

    private Product createProducts(Boolean available, String description, String name, String type) {
        Product product;
        Random r = new Random();
        Float price = 20.21F + r.nextFloat() * 213F;
        product = Product.builder()
                .available(available)
                .description(description)
                .name(name)
                .price(price)
                .type(type)
                .build();
        return product;
    }

    private void setDependencies(Product product, Image image, Building building) {
        product.setImage(image);
        product.setBuilding(building);
        productService.addProduct(product);
    }

    private void showStock() {
        System.out.println("_____products____");
        System.out.println(Arrays.toString(productService.getAll().toArray()));

        System.out.println("______buildings______");
        System.out.println(buildingService.getAll());
    }

    private byte[] toByteArray(String path) {
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

        bufferedImage = scale(bufferedImage, 100, 100);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "jpg", baos);
        } catch (IOException e) {
            log.error("meh image io");
        }

        return baos.toByteArray();
    }

    public BufferedImage scale(BufferedImage img, int targetWidth, int targetHeight) {

        int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage ret = img;
        BufferedImage scratchImage = null;
        Graphics2D g2 = null;

        int w = img.getWidth();
        int h = img.getHeight();

        int prevW = w;
        int prevH = h;

        do {
            if (w > targetWidth) {
                w /= 2;
                w = (w < targetWidth) ? targetWidth : w;
            }

            if (h > targetHeight) {
                h /= 2;
                h = (h < targetHeight) ? targetHeight : h;
            }

            if (scratchImage == null) {
                scratchImage = new BufferedImage(w, h, type);
                g2 = scratchImage.createGraphics();
            }

            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(ret, 0, 0, w, h, 0, 0, prevW, prevH, null);

            prevW = w;
            prevH = h;
            ret = scratchImage;
        } while (w != targetWidth || h != targetHeight);

        if (g2 != null) {
            g2.dispose();
        }

        if (targetWidth != ret.getWidth() || targetHeight != ret.getHeight()) {
            scratchImage = new BufferedImage(targetWidth, targetHeight, type);
            g2 = scratchImage.createGraphics();
            g2.drawImage(ret, 0, 0, null);
            g2.dispose();
            ret = scratchImage;
        }

        return ret;

    }

    @Deprecated
    private Blob toBlob(String path) {
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

        //масштабирование
        int w = bufferedImage.getWidth();
        int h = bufferedImage.getHeight();
        BufferedImage after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        AffineTransform at = new AffineTransform();
        at.scale(0.2, 0.2);
        AffineTransformOp scaleOp =
                new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        after = scaleOp.filter(bufferedImage, after);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(after, "jpg", baos);
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
