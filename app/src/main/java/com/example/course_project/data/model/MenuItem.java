package com.example.course_project.data.model;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A menu item representing a piece of content.
 */
@Getter
@RequiredArgsConstructor
public class MenuItem {
    public static final List<MenuItem> ITEMS = new ArrayList<>();

    private static final int COUNT = 10;

    static {
        for (int i = 1; i <= COUNT; i++) {
            ITEMS.add(createMenuItem(i, null,"Блюдо " + i, i, "Item " + i));
        }
    }

    private static MenuItem createMenuItem(long id, Bitmap image, String name, int price, String description) {
        Random random = new Random();
        int randomNumber = random.nextInt(price);
        return new MenuItem(id, image, name, randomNumber + "₽", description);
    }

    private final long id;
    private final Bitmap image;
    private final String name;
    private final String price;
    private final String description;

    @NonNull
    @Override
    public String toString() {
        return name + " price: " + price + " description: " + description;
    }
}
