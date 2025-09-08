package com.hustVN.otherShopYet.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hustVN.otherShopYet.model.entity.OrderDetail;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findByOrderId(Long orderId);
}
