package ru.cafeteriaitmo.server.domain.entity;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

/** Класс Product - продукция (блюдо или напиток) */
@Getter
@ToString
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    /** поле id - уникальный номер */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "building_id")
    @NotNull
    private Building building;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Image image;
}
