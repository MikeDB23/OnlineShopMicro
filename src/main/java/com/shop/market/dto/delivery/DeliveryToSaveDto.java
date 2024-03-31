package com.shop.market.dto.delivery;

import com.shop.market.entities.Order;

public record DeliveryToSaveDto(
                            Long id,
                            Order order,
                            String address,
                            String company,
                            Integer waybillNumber
) {}