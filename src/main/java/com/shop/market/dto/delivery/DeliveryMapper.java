package com.shop.market.dto.delivery;

import com.shop.market.entities.Delivery;

public interface DeliveryMapper {
    Delivery dtoToEntity(DeliveryDto deliveryDto);
    Delivery saveDtoToEntity(DeliveryToSaveDto deliveryToSaveDto);
    DeliveryDto entityToDto(Delivery delivery);
}
