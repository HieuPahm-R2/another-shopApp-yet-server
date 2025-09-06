package com.hustVN.otherShopYet.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hustVN.otherShopYet.model.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPhoneNumber(String phoneNumber);

    boolean existsByPhoneNumber(String phoneNumber);
}
