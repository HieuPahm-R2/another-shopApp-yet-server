package com.hustVN.otherShopYet.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 350)
    private String name;

    private Double price;

    @Column(name = "description")
    private String description;

    @Column(name = "thumbnail", nullable = true, length = 300)
    private String thumbnail;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
