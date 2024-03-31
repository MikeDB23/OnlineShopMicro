package com.shop.market.dto.payment;

import java.time.LocalDateTime;

import com.shop.market.entities.Order;
import com.shop.market.entities.PaymentMethod;

public record PaymentToSaveDto(
                            Order order,
                            Double totalPayment,
                            LocalDateTime timeOfPayment,
                            PaymentMethod paymentMethod
) {}
