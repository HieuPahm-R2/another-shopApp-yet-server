package com.hustVN.otherShopYet.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "order_details")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @Column(name = "number_of_products", nullable = false)
    private int numberOfProducts;
    @Column(name = "price", nullable = false)
    private Float price;
    @Column(name = "total_money", nullable = false)
    private Float totalMoney;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
