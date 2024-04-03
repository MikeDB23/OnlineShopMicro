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
import com.shop.market.Utils.Status;
import com.shop.market.dto.client.ClientDto;
import com.shop.market.dto.client.ClientMapper;
import com.shop.market.dto.order.OrderDto;
import com.shop.market.dto.order.OrderMapper;
import com.shop.market.dto.order.OrderToSaveDto;
import com.shop.market.dto.orderItem.OrderItemDto;
import com.shop.market.dto.orderItem.OrderItemMapper;
import com.shop.market.entities.Client;
import com.shop.market.entities.Order;
import com.shop.market.entities.OrderItem;
import com.shop.market.entities.Product;
import com.shop.market.repository.ClientRepository;
import com.shop.market.repository.OrderItemRepository;
import com.shop.market.repository.OrderRepository;
import com.shop.market.service.order.OrderServiceImpl;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private OrderItemMapper orderItemMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    List<Client> clients = TestUtil.genClients();
    Client client = clients.get(0);
    List<Order> orders = TestUtil.genOrders(clients);
    Order order = orders.get(0);
    ClientDto clientDto = new ClientDto(1l, client.getName(), client.getEmail(), client.getAddress());
    OrderDto orderDto = new OrderDto(1l, clientDto, order.getTimeOfOrder(), order.getStatus());
    List<Order> orderByIdStatus = new ArrayList<>();
    List<Product> products = TestUtil.genProduct();
    List<OrderItem> orderItems = TestUtil.genOrderItems(orders, products);

    @BeforeEach
    void setUp(){
        client.setId(1l);
        order.setId(1l);
        orderByIdStatus.add(orders.get(0));
        orderByIdStatus.add(orders.get(2));
    }

    @Test
    void testDeleteOrder() {
        //Given an order, when delete, do nothing
        Long orderId = 1l;
        given(orderRepository.findById(orderId)).willReturn(Optional.of(order));
        willDoNothing().given(orderRepository).delete(any());
        orderService.deleteOrder(orderId);

        verify(orderRepository, times(1)).delete(any());
    }

    @Test
    void testFindByClientIdAndStatus() {
        //Given an Id and Status, when find by Id and Status, return Order
        given(orderRepository.findByClientIdAndStatus(client.getId(), order.getStatus())).willReturn(orderByIdStatus);
        List<OrderDto> answerOrders = orderService.findByClientIdAndStatus(order.getId(), order.getStatus());
        assertThat(answerOrders).isNotNull();
        assertThat(answerOrders).asList().hasSize(2);
    }

    @Test
    void testFindByItemsPerOrder() {
        // Given an order, when find items, return items
        given(orderRepository.findByItemsPerOrder(client.getId())).willReturn(orderItems);
        List<OrderItemDto> retuItems = orderService.findByItemsPerOrder(1l);
        assertThat(retuItems).isNotNull();
        assertThat(retuItems).asList().hasSize(6);
    }

    @Test
    void testFindByTimeOfOrderBetween() {
        // Given two times, when find between, return orders
        given(orderRepository.findByTimeOfOrderBetween(any(), any())).willReturn(orderByIdStatus);
        List<OrderDto> retuValues = orderService.findByTimeOfOrderBetween(order.getTimeOfOrder(), order.getTimeOfOrder());
        assertThat(retuValues).isNotNull();
    }

    @Test
    void testFindOrderById() {
        // Given an Id, when find by Id, return order
        given(orderRepository.findById(order.getId())).willReturn(Optional.of(order));
        given(orderMapper.entityToDto(any())).willReturn(orderDto);
        OrderDto retuValue = orderService.findOrderById(1l);
        assertThat(retuValue).isNotNull();
        assertThat(retuValue.id()).isEqualTo(1l);
    }

    @Test
    void testGetAllOrders() {
        // When get all, return orders
        given(orderRepository.findAll()).willReturn(orders);
        List<OrderDto> retuList = orderService.getAllOrders();
        assertThat(retuList).isNotNull();
        assertThat(retuList).asList().hasSize(7);
    }

    @Test
    void testSaveOrder() {
        // GIven an order, when save order, return order
        given(orderMapper.saveDtoToEntity(any())).willReturn(order);
        given(orderMapper.entityToDto(any())).willReturn(orderDto);
        given(orderRepository.save(any())).willReturn(order);
        OrderToSaveDto orderToSave = new OrderToSaveDto(clientDto, order.getTimeOfOrder(), order.getStatus());
        OrderDto retuValue = orderService.saveOrder(orderToSave);
        assertThat(retuValue).isNotNull();
        assertThat(retuValue.id()).isEqualTo(1l);
    }

    @Test
    void testUpdateOrder() {
        // Given an order, when update order, teturn order
        OrderDto orderToUpdate = new OrderDto(1l, clientDto, order.getTimeOfOrder(), Status.SENT);
        Order orderUpdate = order;
        orderUpdate.setStatus(Status.SENT);
        given(orderMapper.entityToDto(any())).willReturn(orderToUpdate);
        given(orderRepository.findById(any())).willReturn(Optional.of(order));
        given(orderRepository.save(any())).willReturn(orderUpdate);
        given(clientMapper.dtoToEntity(any())).willReturn(client);
        OrderToSaveDto orderToSave = new OrderToSaveDto(clientDto, order.getTimeOfOrder(), Status.SENT);
        OrderDto retuValue = orderService.updateOrder(1l, orderToSave);
        assertThat(retuValue).isNotNull();
        assertThat(retuValue.id()).isEqualTo(1l);
        assertThat(retuValue.status()).isEqualTo(Status.SENT);
    }
}
