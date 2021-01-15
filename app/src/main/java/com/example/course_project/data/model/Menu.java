package com.example.course_project.data.model;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;

/**
 * Model class for providing content for user interfaces
 * <p>
 */
public class Menu {
    /**
     * An array of items.
     */
    public static final List<MenuItem> ITEMS = new ArrayList<>();

    private static final int COUNT = 10;

    static {
        for (int i = 1; i <= COUNT; i++) {
            ITEMS.add(createMenuItem(i, "Блюдо " + i, i, "Item " + i, i));
        }
    }

    private static MenuItem createMenuItem(long id, String name, int price, String description, int count) {
        Random random = new Random();
        int randomNumber = random.nextInt(price);
        return new MenuItem(id, name, randomNumber + "₽", description, count);
    }

    /**
     * A menu item representing a piece of content.
     */
    @Getter
    @RequiredArgsConstructor
    public static class MenuItem {
        private final long id;
        private Bitmap image;
        private final String name;
        private final String price;
        private final String description;
        private final int count;

        @NonNull
        @Override
        public String toString() {
            return name + " price: " + price + " description: " + description + " count: " + count;
        }
    }
}