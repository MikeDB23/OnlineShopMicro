package com.shop.market.dto.order;

import java.time.LocalDateTime;

import com.shop.market.Utils.Status;
import com.shop.market.dto.client.ClientDto;

public record OrderDto(
                        Long id,
                        ClientDto client,
                        LocalDateTime timeOfOrder,
                        Status status
){}

