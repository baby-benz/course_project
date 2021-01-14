package ru.cafeteriaitmo.server.controller.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cafeteriaitmo.server.controller.exception.NoEntityException;
import ru.cafeteriaitmo.server.service.ProductService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
public class ImageWriterController {
    private final ProductService productService;

    @GetMapping("/{productId}")
    public String checkImageAsFileFromByte(@PathVariable Long productId) throws NoEntityException {
        byte[] imageBytes = productService.getProduct(productId).getImage().getImage();

        ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
        BufferedImage image = null;
        try {
            image = ImageIO.read(bais);
        } catch (IOException e) {
            log.error("wtf man, it is hard for decoding!!!");
        }
        File outputfile = new File("image_test.jpg");
        try {
            ImageIO.write(image, "jpg", outputfile);
        } catch (IOException e) {
            log.error("wtf man, it is hard for writing as file!!!");
        }
        return "check image_test.jpg";
    }
}
