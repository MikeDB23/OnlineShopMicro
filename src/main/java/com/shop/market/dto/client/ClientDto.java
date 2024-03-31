package com.shop.market.dto.client;

import java.util.Collections;
import java.util.List;

import com.shop.market.entities.Order;

public record ClientDto(
                        Long id, 
                        String name, 
                        String email, 
                        String address, 
                        List<Order> orders
) {
    public List<Order> orders(){
        return Collections.unmodifiableList(orders);
    }
}
