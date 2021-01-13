package ru.cafeteriaitmo.server.domain.entity;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

/** Класс Product - продукция (блюдо или напиток) */
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    /** поле id - уникальный номер */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** поле name - название продукции */
    @Column(columnDefinition = "varchar(64)")
    @NotNull
    private String name;

    /** поле price - цена в рублях */
    @Column
    @NotNull
    private Float price;

    /** поле available - присутствие в наличии */
    @Column(columnDefinition = "boolean default true")
    private Boolean available;

    /** поле available - присутствие в наличии */
    @Column(columnDefinition = "TEXT")
    private String description;

    /** поле type - тип продукции (гарнир, напиток, десерт и т.п.) */
    @Column
    private String type;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "building_products",
            joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "building_id", referencedColumnName = "id"))
    private Building building;

    @Setter
    @OneToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL})
    @JoinTable(
            name = "product_image",
            joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "image_id", referencedColumnName = "id"))
    private Image image;

    @Override
    public String toString() {
        return new StringBuilder()
                .append("Продукт: ")
                .append(name)
                .append(" Цена: ")
                .append(price)
                .append(" В наличии: ")
                .append(available)
                .append(" Тип: ")
                .append(type)
                .append(" Корпус: ")
                .append(building.getName())
                .append("\n")
                .toString();
    }
}
