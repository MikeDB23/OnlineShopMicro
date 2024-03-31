package com.shop.market.dto.order;

import java.time.LocalDateTime;

import com.shop.market.entities.Client;
import com.shop.market.entities.Status;

public record OrderToSaveDto(
                        Client client,
                        LocalDateTime timeOfOrder,
                        Status status
) {}
