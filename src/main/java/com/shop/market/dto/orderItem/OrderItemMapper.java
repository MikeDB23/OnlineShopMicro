package com.shop.market.dto.orderItem;

import org.mapstruct.Mapper;

import com.shop.market.entities.OrderItem;

@Mapper
public interface OrderItemMapper {
    OrderItem dtoToEntity(OrderItemDto orderItemDto);
    OrderItem saveDtoToEntity(OrderItemToSaveDto orderItemToSaveDto);
    OrderItemDto entityToDto(OrderItem orderItem);
}
