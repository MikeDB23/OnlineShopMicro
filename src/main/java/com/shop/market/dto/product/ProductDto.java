package com.shop.market.dto.product;

import java.util.Collections;
import java.util.List;

import com.shop.market.entities.OrderItem;

public record ProductDto(
                         Long id,
                         String name,
                         Double price,
                         Integer stock,
                         List<OrderItem> orders
) {
    public List<OrderItem> orders(){
        return Collections.unmodifiableList(orders);
    } 
}
