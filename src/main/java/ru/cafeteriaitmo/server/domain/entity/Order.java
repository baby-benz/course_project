package ru.cafeteriaitmo.server.domain.entity;

import com.sun.istack.NotNull;
import lombok.*;
import ru.cafeteriaitmo.server.domain.enums.Status;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateAdded;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private User user;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Collection<Product> productsOrdered;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Building building;
}
