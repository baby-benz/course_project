package ru.cafeteriaitmo.server.domain.entity;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
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
@Table(name = "orders")
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

    @Column(columnDefinition = "varchar(8)")
    private String monitorCode;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL})
    @JoinTable(
            name = "user_orders",
            joinColumns = @JoinColumn(name = "order_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private User user;

    @OneToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL})
    @JoinTable(
            name = "order_products",
            joinColumns = @JoinColumn(name = "order_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"))
    private Collection<Product> products;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL})
    @JoinTable(
            name = "building_orders",
            joinColumns = @JoinColumn(name = "order_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "building_id", referencedColumnName = "id"))
    private Building building;
}
