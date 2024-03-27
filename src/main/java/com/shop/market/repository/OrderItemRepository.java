package com.shop.market.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.market.entities.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    
}
