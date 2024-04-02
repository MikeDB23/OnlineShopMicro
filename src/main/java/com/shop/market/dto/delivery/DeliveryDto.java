package com.shop.market.dto.delivery;

import com.shop.market.dto.order.OrderDto;

public record DeliveryDto(
                            Long id,
                            OrderDto order,
                            String address,
                            String company,
                            Integer waybillNumber
) {}
