package com.example.course_project.data.model;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class CartItem {
    private final long id;
    private final Bitmap image;
    private final String name;
    private final String price;
    @Setter
    private int count;

    @NonNull
    @Override
    public String toString() {
        return name + " price: " + price + " count: " + count;
    }
}
