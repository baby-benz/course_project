package com.example.workerinterface.productdto;

import java.io.Serializable;

public class ProductDTO {
        public ProductDTO(String name, double price, Boolean available, String description, String type, byte[] image) {
                this.name = name;
                this.price = (float)price;
                this.available = available;
                this.description = description;
                this.type = type;
                this.image = image;
        }

        private String name;
        private float price;
        private Boolean available;
        private String description;
        private String type;
        private byte[] image;

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

        public void setImage(byte[] image) {
                this.image = image;
        }
}
