package com.shop.market.dto.order;

import java.time.LocalDateTime;

import com.shop.market.Utils.Status;
import com.shop.market.dto.client.ClientDto;

public record OrderToSaveDto(
                        ClientDto client,
                        LocalDateTime timeOfOrder,
                        Status status
) {}
