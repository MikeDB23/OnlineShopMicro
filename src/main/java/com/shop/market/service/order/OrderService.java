package com.shop.market.service.order;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.shop.market.Utils.Status;
import com.shop.market.dto.order.OrderDto;
import com.shop.market.dto.order.OrderToSaveDto;
import com.shop.market.dto.orderItem.OrderItemDto;
import com.shop.market.entities.Client;

@Service
public interface OrderService {
    OrderDto saveOrder(OrderToSaveDto orderToSaveDto);
    OrderDto updateOrder(Long id, OrderToSaveDto orderToSaveDto);
    OrderDto findOrderById(Long id);
    void deleteOrder(Long id);
    List<OrderDto> getAllOrders(); 

    List<OrderDto> findByTimeOfOrderBetween(LocalDateTime firstDate, LocalDateTime endDate);
    List<OrderDto> findByClientIdAndStatus(Long clientId, Status status);
    List<OrderItemDto> findByItemsPerOrder(Long clientId);
}
