package com.shop.market.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shop.market.entities.Delivery;
import com.shop.market.entities.Order;
import com.shop.market.entities.Status;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    Optional<Delivery> findByOrder(Order order);
    List<Delivery> findByCompany(String company);
    @Query("SELECT d FROM Delivery d WHERE d.order.status = :s ")
    List<Delivery> findByStatus(@Param("s") Status status);
}
