package ru.cafeteriaitmo.server.domain.entity;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.Collection;

/** Класс Building - корпус Университета (Кронверкский, Ломоносова и т.п.) */
@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(fetch = FetchType.LAZY,
        cascade = {CascadeType.ALL})
    @JoinTable(
        name = "building_products",
        joinColumns = @JoinColumn(name = "building_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"))
    private Collection<Product> products;

    @OneToMany(fetch = FetchType.LAZY,
        cascade = {CascadeType.ALL})
    @JoinTable(
        name = "building_orders",
        joinColumns = @JoinColumn(name = "building_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "order_id", referencedColumnName = "id"))
    private Collection<Order> orders;

    @Column(columnDefinition = "varchar(32)")
    @NotNull
    @ToString.Include
    private String name;

    @Column(columnDefinition = "varchar(64)")
    private String address;
}
