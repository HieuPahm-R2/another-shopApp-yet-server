package com.hustVN.otherShopYet.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hustVN.otherShopYet.model.entity.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}
