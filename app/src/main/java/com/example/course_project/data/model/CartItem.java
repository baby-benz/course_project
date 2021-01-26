package com.example.course_project.data.model;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@AllArgsConstructor
public class CartItem {
    public static final List<CartItem> ITEMS = new ArrayList<>();

    private final long id;
    //private final Bitmap image;
    private final String name;
    private final String price;
    @Setter
    private int count;

    private static final int COUNT = 10;

    static {
        for (int i = 1; i <= COUNT; i++) {
            ITEMS.add(createCartItem(i, "Блюдо " + i, i, i));
        }
    }

    private static CartItem createCartItem(long id, String name, int price, int count) {
        Random random = new Random();
        int randomNumber = random.nextInt(price);
        return new CartItem(id, name, randomNumber + "₽", count);
    }

    @NonNull
    @Override
    public String toString() {
        return name + " price: " + price + " count: " + count;
    }
}
