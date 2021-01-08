package ru.cafeteriaitmo.server.domain.entity;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.Collection;

/** Класс Building - корпус Университета (Кронверкский, Ломоносова и т.п.) */
@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @MapsId
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Collection<Product> products;

    @MapsId
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Collection<Order> orders;

    @Column(columnDefinition = "varchar(32)")
    @NotNull
    private String name;

    @Column(columnDefinition = "varchar(64)")
    private String address;
}
