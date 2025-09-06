package com.hustVN.otherShopYet.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hustVN.otherShopYet.model.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
