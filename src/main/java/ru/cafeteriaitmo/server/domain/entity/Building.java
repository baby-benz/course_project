package ru.cafeteriaitmo.server.domain.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Building {
    @Id
    private Long id;
    private String name;
    private String address;
}
