package com.shop.market.dto.orderItem;

import com.shop.market.entities.Order;
import com.shop.market.entities.Product;

public record OrderItemToSaveDto(
                            Order order,
                            Product product,
                            Integer amount,
                            Double pricePerUnit
) {}
