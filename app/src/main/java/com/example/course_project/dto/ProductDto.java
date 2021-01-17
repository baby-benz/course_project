package com.example.course_project.dto;

import android.util.Base64;

import lombok.Data;

@Data
public class ProductDto {
    public ProductDto(String name, double price, Boolean available, String description, String type, String image, String nameBuilding) {
        this.name = name;
        this.price = (float)price;
        this.available = available;
        this.description = description;
        this.type = type;
        this.image = android.util.Base64.decode(image, Base64.DEFAULT);
        this.nameBuilding = nameBuilding;
    }

    private String name;
    private float price;
    private Boolean available;
    private String description;
    private String type;
    private byte[] image;
    private String nameBuilding;
}