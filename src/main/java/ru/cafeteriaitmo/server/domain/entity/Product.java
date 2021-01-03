package ru.cafeteriaitmo.server.domain.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Product {
    @Id
    private String id;
    private String name;
    private Boolean available;
}
