package ru.cafeteriaitmo.server.domain.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Order {
    @Id
    private Long id;
}
