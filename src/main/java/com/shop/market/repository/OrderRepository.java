package com.shop.market.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.market.entities.Client;
import com.shop.market.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByTimeOfOrderBetween(LocalDate firstDate, LocalDate endDate);
    List<Order> findByClientAndState(Client client, String status);
    //Mirar como se hace el tercero
}
