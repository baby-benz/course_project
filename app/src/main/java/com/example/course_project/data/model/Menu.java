package com.example.course_project.data.model;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.*;

/**
 * Model class for providing content for user interfaces
 * <p>
 */
@RequiredArgsConstructor
public class Menu {
    /**
     * An array of items.
     */
    public static final List<MenuItem> ITEMS = new ArrayList<>();

    private static final int COUNT = 10;

    static {
        for (int i = 1; i <= COUNT; i++) {
            ITEMS.add(createMenuItem("Блюдо " + i, i, "Item " + i, i));
        }
    }

    private static MenuItem createMenuItem(String name, int price, String description, int count) {
        Random random = new Random();
        int randomNumber = random.nextInt(price);
        return new MenuItem(name, randomNumber + "₽", description, count);
    }

    /**
     * A menu item representing a piece of content.
     */
    @RequiredArgsConstructor
    public static class MenuItem {
        private Bitmap mImage;
        private final String mName;
        private final String mPrice;
        private final String mDescription;
        private final int mCount;

        public Bitmap getImage() {
            return mImage;
        }

        public String getName() {
            return mName;
        }

        public String getPrice() {
            return mPrice;
        }

        public String getDescription() {
            return mDescription;
        }

        public int getCount() {
            return mCount;
        }

        @NonNull
        @Override
        public String toString() {
            return mName + " price: " + mPrice + " description: " + mDescription + " count: " + mCount;
        }
    }
}