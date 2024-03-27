package com.shop.market.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.market.entities.Order;
import com.shop.market.entities.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long>{
    List<Payment> findBytimeOfPaymentBetween(LocalDate firstDate, LocalDate endDate);
    List<Payment> findByOrderIdAndpaymentMethod(Order order, String paymentMethod);
}
