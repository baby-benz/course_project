package ru.cafeteriaitmo.server.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProductDto implements Serializable {
    private static final long serialVersionUID = 2116976449580433258L;
    private Long id;
    private String name;
    private Float price;
    private Boolean available;
    private String description;
    private String type;

    private byte[] image;
    private String nameBuilding;
}
