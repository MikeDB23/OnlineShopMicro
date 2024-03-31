package com.shop.market.service.orderItem;

import java.util.List;

import com.shop.market.dto.orderItem.OrderItemDto;
import com.shop.market.dto.orderItem.OrderItemMapper;
import com.shop.market.dto.orderItem.OrderItemToSaveDto;
import com.shop.market.entities.OrderItem;
import com.shop.market.entities.Product;
import com.shop.market.exceptions.NotAbleToDeleteException;
import com.shop.market.exceptions.NotFoundException;
import com.shop.market.repository.OrderItemRepository;

public class orderItemServiceImpl implements orderItemService{
    OrderItemRepository orderItemRepository;
    OrderItemMapper orderItemMapper;
    public orderItemServiceImpl(OrderItemRepository orderItemRepository, OrderItemMapper orderItemMapper) {
        this.orderItemRepository = orderItemRepository;
        this.orderItemMapper = orderItemMapper;
    }

    @Override
    public void deleteOrderItem(Long id) {
        OrderItem orderItem = orderItemRepository.findById(id)
                                .orElseThrow(() -> new NotAbleToDeleteException("order item not found"));
        orderItemRepository.delete(orderItem);
    }
    

    @Override
    public OrderItemDto findOrderItemById(Long id) {
        OrderItem orderItem = orderItemRepository.findById(id)
                                .orElseThrow(() -> new NotFoundException("order item not found"));
        return orderItemMapper.entityToDto(orderItem);
    }

    @Override
    public List<OrderItemDto> getAllOrderItems() {
        List<OrderItem> orderItems = orderItemRepository.findAll();
        return orderItems.stream()
                .map(orderItem -> orderItemMapper.entityToDto(orderItem))
                .toList();
    }


    @Override
    public OrderItemDto saveOrderItem(OrderItemToSaveDto orderItemToSaveDto) {
        OrderItem orderItem = orderItemMapper.saveDtoToEntity(orderItemToSaveDto);
        OrderItem savedOrderItem = orderItemRepository.save(orderItem);
        return orderItemMapper.entityToDto(savedOrderItem);
    }
    @Override
    public OrderItemDto updateOrderItem(Long id, OrderItemToSaveDto orderItemToSaveDto) {
        return orderItemRepository.findById(id)
                .map(orderItemDB -> {
                    orderItemDB.setAmount(orderItemToSaveDto.amount());
                    orderItemDB.setOrder(orderItemToSaveDto.order());
                    orderItemDB.setPricePerUnit(orderItemToSaveDto.pricePerUnit());
                    orderItemDB.setProduct(orderItemToSaveDto.product());
                    
                    OrderItem savedOrderItem = orderItemRepository.save(orderItemDB);
                    return orderItemMapper.entityToDto(savedOrderItem);
                }).orElseThrow(() -> new NotFoundException("order item not found"));
    }

    @Override
    public List<OrderItemDto> findByOrderId(Long orderId) {
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
        return orderItems.stream()
                .map(orderItem -> orderItemMapper.entityToDto(orderItem))
                .toList();
    }
    
    @Override
    public List<OrderItemDto> findByProductId(Long productId) {
        List<OrderItem> orderItems = orderItemRepository.findByProductId(productId);
        return orderItems.stream()
                .map(orderItem -> orderItemMapper.entityToDto(orderItem))
                .toList();
    }

    @Override
    public Double getProductSales(Long productId) {
        return orderItemRepository.getProductSales(productId);
    }
}
