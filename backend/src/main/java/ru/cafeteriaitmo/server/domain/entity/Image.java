package ru.cafeteriaitmo.server.domain.entity;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Blob;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column
    @Type(type="org.hibernate.type.BinaryType")
    private byte[] image;

    @OneToOne(fetch = FetchType.EAGER,
            cascade = {CascadeType.ALL})
    @JoinTable(
            name = "product_image",
            joinColumns = @JoinColumn(name = "image_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"))
    private Product product;
}
