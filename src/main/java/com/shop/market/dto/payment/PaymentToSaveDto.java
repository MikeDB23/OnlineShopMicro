package com.shop.market.dto.payment;

import java.time.LocalDateTime;

import com.shop.market.Utils.PaymentMethod;
import com.shop.market.dto.order.OrderDto;

public record PaymentToSaveDto(
                            OrderDto order,
                            Double totalPayment,
                            LocalDateTime timeOfPayment,
                            PaymentMethod paymentMethod
) {}
