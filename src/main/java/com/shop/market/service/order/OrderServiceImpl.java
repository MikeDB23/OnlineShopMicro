package com.shop.market.service.order;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.shop.market.Utils.Status;
import com.shop.market.dto.order.OrderDto;
import com.shop.market.dto.order.OrderMapper;
import com.shop.market.dto.order.OrderToSaveDto;
import com.shop.market.dto.orderItem.OrderItemDto;
import com.shop.market.dto.orderItem.OrderItemMapper;
import com.shop.market.entities.Order;
import com.shop.market.entities.OrderItem;
import com.shop.market.exceptions.NotAbleToDeleteException;
import com.shop.market.exceptions.NotFoundException;
import com.shop.market.repository.OrderRepository;

@Service
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    
    public OrderServiceImpl(OrderRepository orderRepository, OrderMapper orderMapper, OrderItemMapper orderItemMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
    }

    @Override
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                        .orElseThrow(() -> new NotAbleToDeleteException("Order not found"));
        orderRepository.delete(order);
    }

    @Override
    public OrderDto findOrderById(Long id) {
        Order order = orderRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Order not found"));
        return orderMapper.entityToDto(order);
    }

    @Override
    public List<OrderDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(order -> orderMapper.entityToDto(order))
                .toList();
    }

    @Override
    public OrderDto saveOrder(OrderToSaveDto orderToSaveDto) {
        Order order = orderMapper.saveDtoToEntity(orderToSaveDto);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.entityToDto(savedOrder);
    }

    @Override
    public OrderDto updateOrder(Long id, OrderToSaveDto order) {
        return orderRepository.findById(id)
                .map(orderDB -> {
                    orderDB.setClient(order.client());
                    orderDB.setStatus(order.status());
                    orderDB.setTimeOfOrder(order.timeOfOrder());
                    
                    Order savedOrder = orderRepository.save(orderDB);
                    return orderMapper.entityToDto(savedOrder);
                })
                .orElseThrow(() -> new NotFoundException("Order not found"));
    }
    
    
    @Override
    public List<OrderDto> findByClientIdAndStatus(Long clientId, Status status) {
        List<Order> orders = orderRepository.findByClientIdAndStatus(clientId, status);
        return orders.stream()
                .map(order -> orderMapper.entityToDto(order))
                .toList();
    }

    @Override
    public List<OrderItemDto> findByItemsPerOrder(Long clientId) {
        List<OrderItem> orderItems = orderRepository.findByItemsPerOrder(clientId);
        return orderItems.stream()
                .map(orderItem -> orderItemMapper.entityToDto(orderItem))
                .toList();
    }

    @Override
    public List<OrderDto> findByTimeOfOrderBetween(LocalDateTime firstDate, LocalDateTime endDate) {
        List<Order> orders = orderRepository.findByTimeOfOrderBetween(firstDate, endDate);
        return orders.stream()
                .map(order -> orderMapper.entityToDto(order))
                .toList();
    }

}
