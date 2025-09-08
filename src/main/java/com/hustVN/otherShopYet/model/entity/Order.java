package com.hustVN.otherShopYet.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "fullname", length = 100)
    private String fullName;
    @Column(name = "email", length = 150)
    private String email;
    @Column(name = "phone_number", length = 50, nullable = false)
    private String phoneNumber;
    @Column(name = "address", length = 200)
    private String address;
    @Column(name = "note", length = 100)
    private String note;
    @Column(name = "order_date")
    private LocalDate orderDate;
    @Column(name = "status")
    private String status;
    @Column(name = "total_money")
    private Float totalMoney;
    @Column(name = "shipping_method")
    private String shippingMethod;
    @Column(name = "shipping_address")
    private String shippingAddress;
    @Column(name = "shipping_date")
    private LocalDate shippingDate;
    @Column(name = "payment_method")
    private String paymentMethod;
    @Column(name = "active")
    private boolean active;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<OrderDetail> orderDetails;

//    @ManyToOne
//    @JoinColumn(name = "coupon_id")
//    @JsonBackReference
//    private Coupon coupon;
}
