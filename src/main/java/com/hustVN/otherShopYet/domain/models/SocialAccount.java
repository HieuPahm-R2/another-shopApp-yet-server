package com.hustVN.otherShopYet.domain.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "social_accounts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SocialAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "provider", nullable = false, length = 20)
    private String provider;
    @Column(name = "provider_id", nullable = false, length = 50)
    private String providerId;
    @Column(name = "name", length = 100)
    private String name;
    @Column(name = "email", length = 150)
    private String email;
}
