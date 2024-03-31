package com.shop.market.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shop.market.Utils.Status;
import com.shop.market.entities.Order;
import com.shop.market.entities.OrderItem;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByTimeOfOrderBetween(LocalDateTime firstDate, LocalDateTime endDate);
    List<Order> findByClientIdAndStatus(Long clientId, Status status);
    @Query("Select i FROM OrderItem i "+ 
            "JOIN FETCH i.product p "+
            "WHERE i.order.client.id = :clientId")
    List<OrderItem> findByItemsPerOrder(@Param("clientId") Long clientId);
}
