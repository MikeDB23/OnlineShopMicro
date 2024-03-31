package com.shop.market.dto.payment;

import java.time.LocalDateTime;

import com.shop.market.Utils.PaymentMethod;
import com.shop.market.entities.Order;

public record PaymentToSaveDto(
                            Order order,
                            Double totalPayment,
                            LocalDateTime timeOfPayment,
                            PaymentMethod paymentMethod
) {}
