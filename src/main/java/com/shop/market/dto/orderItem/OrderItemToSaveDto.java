package com.shop.market.dto.orderItem;


import com.shop.market.dto.order.OrderDto;
import com.shop.market.dto.product.ProductDto;

public record OrderItemToSaveDto(
                            OrderDto order,
                            ProductDto product,
                            Integer amount,
                            Double pricePerUnit
) {}
