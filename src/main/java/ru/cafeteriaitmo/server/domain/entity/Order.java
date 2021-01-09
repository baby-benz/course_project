package ru.cafeteriaitmo.server.domain.entity;

import com.sun.istack.NotNull;
import lombok.*;
import ru.cafeteriaitmo.server.domain.enums.Status;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

@Getter
@Setter
@Builder
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

    @NotNull
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateTimeOrderedOn;

    @Enumerated(EnumType.STRING)
    private Status status;

    @JoinColumn(name = "user_id")
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "building")
    private Collection<Product> productsOrdered;

    @JoinColumn(name = "building_id")
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Building building;
}
