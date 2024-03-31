package com.shop.market.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.market.Utils.PaymentMethod;
import com.shop.market.entities.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long>{
    List<Payment> findByTimeOfPaymentBetween(LocalDateTime firstDate, LocalDateTime endDate);
    Optional<Payment> findByOrderIdAndPaymentMethod(Long order, PaymentMethod paymentMethod);
}
