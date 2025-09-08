package com.hustVN.otherShopYet.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_details")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number_of_products", nullable = false)
    private int numberOfProducts;
    @Column(name = "price", nullable = false)
    private Float price;
    @Column(name = "total_money", nullable = false)
    private Float totalMoney;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
