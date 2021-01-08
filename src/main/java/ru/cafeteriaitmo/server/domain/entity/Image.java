package ru.cafeteriaitmo.server.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @MapsId
    @OneToOne
    private Product product;

    @Lob
    @Column(columnDefinition="BYTEA")
    private byte[] image;
}
