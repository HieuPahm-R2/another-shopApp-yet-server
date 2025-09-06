package com.hustVN.otherShopYet.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hustVN.otherShopYet.model.entity.OrderDetails;

public interface OrderDetailRepository extends JpaRepository<OrderDetails, Long> {
}
