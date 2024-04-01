package com.shop.market.dto.order;

import java.time.LocalDateTime;

import com.shop.market.Utils.Status;
import com.shop.market.entities.Client;

public record OrderDto(
                        Long id,
                        Client client,
                        LocalDateTime timeOfOrder,
                        Status status
){}

