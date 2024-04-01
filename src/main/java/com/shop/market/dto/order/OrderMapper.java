package com.shop.market.dto.order;

import org.mapstruct.Mapper;

import com.shop.market.entities.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order dtoToEntity(OrderDto orderDto);
    Order saveDtoToEntity(OrderToSaveDto orderToSaveDto);
    OrderDto entityToDto(Order order);
}
