package com.shop.market.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.repository.query.Param;

import com.shop.market.Utils.Status;
import com.shop.market.entities.Delivery;
// import com.shop.market.entities.Order;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    Optional<Delivery> findByOrderId(Long orderId);
    List<Delivery> findByCompany(String company);
    // @Query("SELECT d FROM Delivery d WHERE d.order.status = :s ")
    // List<Delivery> findByOrderStatus(@Param("s") Status status);
    List<Delivery> findByOrderStatus(Status status);
}
