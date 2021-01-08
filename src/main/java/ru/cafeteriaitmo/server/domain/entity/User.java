package ru.cafeteriaitmo.server.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String surname;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "building")
    private List<Order> orders;
}
