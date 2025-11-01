package com.hustVN.otherShopYet.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hustVN.otherShopYet.model.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    @Query("SELECT u FROM User u WHERE (:keyword IS NULL OR :keyword = '' " +
            "OR u.fullName LIKE %:keyword% " + "OR u.phoneNumber LIKE %:keyword%) "+
            "OR u.email LIKE %:keyword% " +"AND LOWER(u.role.name) = 'user'")
    Page<User> findAll(@Param("keyword")String keyword, Pageable pageable);
}
