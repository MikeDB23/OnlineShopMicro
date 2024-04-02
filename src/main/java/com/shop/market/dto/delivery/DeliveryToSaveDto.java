package com.shop.market.dto.delivery;

import com.shop.market.dto.order.OrderDto;

public record DeliveryToSaveDto(
                            OrderDto order,
                            String address,
                            String company,
                            Integer waybillNumber
) {}