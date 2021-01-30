package com.example.workerinterface.dto;

import java.util.Base64;

public class ProductDTO {
        public ProductDTO() {

        }
        public ProductDTO(int id, String name, double price, Boolean available, String description, String type, String image, String nameBuilding) {
                this.id = id;
                this.name = name;
                this.price = (float)price;
                this.available = available;
                this.description = description;
                this.type = type;
                this.image = Base64.getDecoder().decode(image);
                this.nameBuilding = nameBuilding;
        }

        private int id;
        private String name;
        private float price;
        private Boolean available;
        private String description;
        private String type;
        private byte[] image;
        private String nameBuilding;

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public Float getPrice() {
                return price;
        }

        public void setPrice(Float price) {
                this.price = price;
        }

        public Boolean getAvailable() {
                return available;
        }

        public void setAvailable(Boolean available) {
                this.available = available;
        }

        public String getDescription() {
                return description;
        }

        public void setDescription(String description) {
                this.description = description;
        }

        public String getType() {
                return type;
        }

        public void setType(String type) {
                this.type = type;
        }

        public byte[] getImage() {
                return image;
        }

        public void setImage(String imageString) {
                this.image = Base64.getDecoder().decode(imageString);
        }

        public String getNameBuilding() {
                return nameBuilding;
        }

        public void setNameBuilding(String nameBuilding) {
                this.nameBuilding = nameBuilding;
        }

        public int getId() {
                return id;
        }

        public void setId(int id) {
                this.id = id;
        }
}
