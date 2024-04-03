package com.shop.market.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.shop.market.TestUtil;
import com.shop.market.dto.client.ClientDto;
import com.shop.market.dto.order.OrderDto;
import com.shop.market.dto.order.OrderMapper;
import com.shop.market.dto.orderItem.OrderItemDto;
import com.shop.market.dto.orderItem.OrderItemMapper;
import com.shop.market.dto.orderItem.OrderItemToSaveDto;
import com.shop.market.dto.product.ProductDto;
import com.shop.market.dto.product.ProductMapper;
import com.shop.market.entities.Client;
import com.shop.market.entities.Order;
import com.shop.market.entities.OrderItem;
import com.shop.market.entities.Product;
import com.shop.market.repository.OrderItemRepository;
import com.shop.market.repository.OrderRepository;
import com.shop.market.repository.ProductRepository;
import com.shop.market.service.orderItem.OrderItemServiceImpl;

@ExtendWith(MockitoExtension.class)
public class OrderItemServiceImplTest {

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private OrderItemMapper orderItemMapper;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderItemServiceImpl orderItemService;
    
    List<Client> clients = TestUtil.genClients();
    Client client = clients.get(0);
    ClientDto clientDto = new ClientDto(1l, client.getName(), client.getEmail(), client.getAddress());
    List<Product> products = TestUtil.genProduct();
    Product product = products.get(0);
    ProductDto productDto = new ProductDto(1l, product.getName(), product.getPrice(), product.getStock());
    List<Order> orders = TestUtil.genOrders(clients);
    Order order = orders.get(0);
    OrderDto orderDto = new OrderDto(1l, clientDto, order.getTimeOfOrder(), order.getStatus());
    List<OrderItem> orderItems = TestUtil.genOrderItems(orders, products);
    OrderItem orderItem = orderItems.get(0);
    OrderItemDto orderItemDto = new OrderItemDto(1l, orderDto, productDto, orderItem.getAmount(), orderItem.getPricePerUnit());
    List<OrderItem> resuList = new ArrayList<>();

    @BeforeEach
    void setUp(){
        product.setId(1l);
        order.setId(1l);
        orderItem.setId(1l);
        resuList.clear();
        resuList.add(orderItem);
    }

    @Test
    void testDeleteOrderItem() {
        // Given an order, when delete, do nothing
        willDoNothing().given(orderItemRepository).delete(any());
        given(orderItemRepository.findById(orderItem.getId())).willReturn(Optional.of(orderItem));
        orderItemService.deleteOrderItem(1l);
        verify(orderItemRepository, times(1)).delete(any());
    }

    @Test
    void testFindByOrderId() {
        // Given an order Id, when find by order Id, return OrderItems
        resuList.add(orderItems.get(1));
        given(orderItemRepository.findByOrderId(order.getId())).willReturn(resuList);
        given(orderItemMapper.entityToDto(any())).willReturn(orderItemDto);
        List<OrderItemDto> retuItemDto = orderItemService.findByOrderId(1l);
        assertThat(retuItemDto).isNotNull();
        assertThat(resuList).asList().hasSize(2);
    }

    @Test
    void testFindByProductId() {
        // Given a product Id, when find by product, return order items
        resuList.add(orderItems.get(5));
        given(orderItemRepository.findByProductId(product.getId())).willReturn(resuList);
        given(orderItemMapper.entityToDto(any())).willReturn(orderItemDto);
        List<OrderItemDto> retuItemDtos = orderItemService.findByProductId(1l);
        assertThat(retuItemDtos).isNotNull();
        assertThat(retuItemDtos).asList().hasSize(2);
    }

    @Test
    void testFindOrderItemById() {
        // Given an order item Id, when find by Id, return order item
        given(orderItemRepository.findById(orderItem.getId())).willReturn(Optional.of(orderItem));
        given(orderItemMapper.entityToDto(any())).willReturn(orderItemDto);
        OrderItemDto retuItemDto = orderItemService.findOrderItemById(1l);
        assertThat(retuItemDto).isNotNull();
        assertThat(retuItemDto.id()).isEqualTo(1l);
    }

    @Test
    void testGetAllOrderItems() {
        // When find all order items, return order items
        given(orderItemRepository.findAll()).willReturn(orderItems);
        List<OrderItemDto> retuItemDtos = orderItemService.getAllOrderItems();
        assertThat(retuItemDtos).isNotNull();
        assertThat(retuItemDtos.size()).isEqualTo(6);
    }

    @Test
    void testGetProductSales() {
        // Given a product, when get sales, return float
        resuList.add(orderItems.get(5));
        Double total = 0.00;
        for (OrderItem item : resuList) {
            total += (item.getPricePerUnit() * item.getAmount());
        }
        given(orderItemRepository.getProductSales(product.getId())).willReturn(total);
        Double result = orderItemService.getProductSales(1l);
        assertThat(result).isEqualTo(966000.00);
    }

    @Test
    void testSaveOrderItem() {
        // Given order item, when save, return order item
        given(orderItemRepository.save(any())).willReturn(orderItem);
        given(orderItemMapper.saveDtoToEntity(any())).willReturn(orderItem);
        given(orderItemMapper.entityToDto(any())).willReturn(orderItemDto);
        OrderItemToSaveDto orderToSaveDto = new OrderItemToSaveDto(orderDto, productDto, orderItem.getAmount(), orderItem.getPricePerUnit());
        OrderItemDto retuItemDto = orderItemService.saveOrderItem(orderToSaveDto);
        assertThat(retuItemDto).isNotNull();
        assertThat(retuItemDto.id()).isEqualTo(1l);
    }

    @Test
    void testUpdateOrderItem() {
        // Given an order item, when update, return order item
        OrderItemDto orderItemToUpdate = new OrderItemDto(1l, orderDto, productDto, 10, orderItem.getPricePerUnit());
        OrderItem updatedItem = orderItem;
        updatedItem.setAmount(10);
        given(orderItemMapper.entityToDto(any())).willReturn(orderItemToUpdate);
        given(orderItemRepository.findById(any())).willReturn(Optional.of(orderItem));
        given(orderItemRepository.save(any())).willReturn(updatedItem);
        given(orderRepository.findById(any())).willReturn(Optional.of(order));
        given(productRepository.findById(any())).willReturn(Optional.of(product));
        OrderItemToSaveDto orderToSaveDto = new OrderItemToSaveDto(orderDto, productDto, 10, orderItem.getPricePerUnit());
        OrderItemDto retuItemDto = orderItemService.updateOrderItem(1l, orderToSaveDto);
        assertThat(retuItemDto).isNotNull();
        assertThat(retuItemDto.id()).isEqualTo(1l);
        assertThat(retuItemDto.amount()).isEqualTo(10);
    }
}
