package com.shop.market.dto.delivery;

import org.mapstruct.Mapper;

import com.shop.market.entities.Delivery;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {
    Delivery dtoToEntity(DeliveryDto deliveryDto);
    Delivery saveDtoToEntity(DeliveryToSaveDto deliveryToSaveDto);
    DeliveryDto entityToDto(Delivery delivery);
}
