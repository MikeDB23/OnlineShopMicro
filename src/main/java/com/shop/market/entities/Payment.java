package com.shop.market.entities;

import java.time.LocalDateTime;

import org.springframework.lang.NonNull;

import com.shop.market.Utils.PaymentMethod;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orderItems")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder

public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "orderId")
    private Order order;
    
    @NonNull
    private Double totalPayment;
    @NonNull
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime timeOfPayment;
    
    private PaymentMethod paymentMethod;
}
