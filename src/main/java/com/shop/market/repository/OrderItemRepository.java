package com.shop.market.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shop.market.entities.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrderId(Long orderId);
    List<OrderItem> findByProductId(Long productId);
    @Query("SELECT SUM(i.amount * i.product.price) FROM OrderItem i WHERE i.product.id = :p")
    Double getProductSales(@Param("p") Long productId);
}
