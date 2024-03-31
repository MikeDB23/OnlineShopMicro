package com.shop.market.dto.order;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import com.shop.market.entities.Client;
import com.shop.market.entities.OrderItem;
import com.shop.market.entities.Status;

public record OrderDto(
                        Long id,
                        Client client,
                        LocalDateTime timeOfOrder,
                        Status status,
                        List<OrderItem> orderItems
) {
    public List<OrderItem> orderItems(){
        return Collections.unmodifiableList(orderItems);
    }
}
