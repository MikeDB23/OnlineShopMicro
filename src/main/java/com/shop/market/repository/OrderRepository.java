package com.shop.market.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shop.market.entities.Client;
import com.shop.market.entities.Order;
import com.shop.market.entities.OrderItem;
import com.shop.market.entities.Status;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByTimeOfOrderBetween(LocalDateTime firstDate, LocalDateTime endDate);
    List<Order> findByClientAndStatus(Client client, Status status);
    //Mirar como se hace el tercero
    @Query("Select i FROM OrderItem i "+ 
            "JOIN FETCH i.product p "+
            "WHERE i.order.client = :client")
    List<OrderItem> findByItemsPerOrder(@Param("client") Client client);
}
