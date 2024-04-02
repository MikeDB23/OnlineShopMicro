package com.shop.market.service.orderItem;

import java.util.List;

import com.shop.market.dto.orderItem.OrderItemDto;
import com.shop.market.dto.orderItem.OrderItemToSaveDto;

public interface OrderItemService {
    OrderItemDto saveOrderItem(OrderItemToSaveDto orderItem);
    OrderItemDto updateOrderItem(Long id, OrderItemToSaveDto orderItemToSaveDto);
    OrderItemDto findOrderItemById(Long id);
    void deleteOrderItem(Long id);
    List<OrderItemDto> getAllOrderItems(); 

    List<OrderItemDto> findByOrderId(Long orderId);
    List<OrderItemDto> findByProductId(Long productId);
    Double getProductSales(Long productId);
}
