package com.hustVN.otherShopYet.domain.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="full_name", length = 50)
    private String fullName;
    @Column(name="phone_number",nullable = false, length = 12)
    private String phoneNumber;
    @Column(name="password", nullable = false, length = 100)
    private String password;
    @Column(name="address", length = 200)
    private String address;

    private boolean active;
    @Column(name="date_of_birth")
    private Date dateOfBirth;
    @Column(name="facebook_account_id")
    private int facebookAccountId;
    @Column(name="google_account_id")
    private int googleAccountId;
    @Column(name="email", nullable = false, length = 50)
    private String email;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
