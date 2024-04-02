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
import com.shop.market.dto.client.ClientMapper;
import com.shop.market.dto.delivery.DeliveryDto;
import com.shop.market.dto.delivery.DeliveryMapper;
import com.shop.market.dto.delivery.DeliveryToSaveDto;
import com.shop.market.dto.order.OrderDto;
import com.shop.market.dto.order.OrderMapper;
import com.shop.market.entities.Client;
import com.shop.market.entities.Delivery;
import com.shop.market.entities.Order;
import com.shop.market.repository.ClientRepository;
import com.shop.market.repository.DeliveryRepository;
import com.shop.market.repository.OrderRepository;
import com.shop.market.service.delivery.DeliveryServiceImpl;

@ExtendWith(MockitoExtension.class)
public class DeliveryServiceImpTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private DeliveryMapper deliveryMapper;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private DeliveryServiceImpl deliveryService;

    List<Client> clients = TestUtil.genClients();
    ClientDto clientDto = new ClientDto(clients.get(0).getId(), clients.get(0).getName(), clients.get(0).getEmail(), clients.get(0).getAddress());
    List<Order> orders = TestUtil.genOrders(clients);
    Order order = orders.get(0);
    OrderDto orderDto = new OrderDto(order.getId(), clientDto, order.getTimeOfOrder(), order.getStatus());
    List<Delivery> deliveries = TestUtil.genDeliveries(orders);
    Delivery delivery = deliveries.get(0);
    DeliveryDto deliveryDto = new DeliveryDto(delivery.getId(), orderDto, delivery.getAddress(), delivery.getCompany(), delivery.getWaybillNumber());
    List<Delivery> answerList = new ArrayList<>();

    @BeforeEach
    void setUp(){
        delivery.setId(1l);
        answerList.clear();
        answerList.add(delivery);
    }

    @Test
    void testDeleteDelivery() {
        //Given a delivery, when delete, do nothing
        Long deliveryId = 1l;
        willDoNothing().given(deliveryRepository).delete(any());
        given(deliveryRepository.findById(deliveryId)).willReturn(Optional.of(delivery));
        deliveryService.deleteDelivery(deliveryId);

        verify(deliveryRepository, times(1)).delete(any());
    }

    @Test
    void testFindByCompany() {
        // Given a company, when find by company, return delivery
        given(deliveryRepository.findByCompany(delivery.getCompany())).willReturn(answerList);
        List<DeliveryDto> retuList = deliveryService.findByCompany(delivery.getCompany());
        assertThat(retuList).isNotNull();
        assertThat(retuList).asList().hasSize(1);
    }

    @Test
    void testFindByOrderId() {
        // Given an order, when find by order id, return delivery
        given(deliveryRepository.findByOrderId(order.getId())).willReturn(Optional.of(delivery));
        given(deliveryMapper.entityToDto(any())).willReturn(deliveryDto);
        DeliveryDto retuDelivery = deliveryService.findByOrderId(order.getId());
        assertThat(retuDelivery).isNotNull();
    }

    @Test
    void testFindByOrderStatus() {
        // Given a status, when find by order status, return delivery
        given(deliveryRepository.findByOrderStatus(order.getStatus())).willReturn(answerList);
        List<DeliveryDto> retuDelivery = deliveryService.findByOrderStatus(order.getStatus());
        assertThat(retuDelivery).isNotNull();
    }

    @Test
    void testFindDeliveryById() {
        // Given an Id, when find by Id, return delivery
        given(deliveryRepository.findById(delivery.getId())).willReturn(Optional.of(delivery));
        given(deliveryMapper.entityToDto(any())).willReturn(deliveryDto);
        DeliveryDto retuValue = deliveryService.findDeliveryById(delivery.getId());
        assertThat(retuValue).isNotNull();
    }

    @Test
    void testGetAllDeliveries() {
        // When find all, return delivery list
        given(deliveryRepository.findAll()).willReturn(deliveries);
        List<DeliveryDto> allList = deliveryService.getAllDeliveries();
        assertThat(allList).isNotNull();
        assertThat(allList).asList().hasSize(7);
    }

    @Test
    void testSaveDelivery() {
        // Given a delivery, when save, return delivery
        given(deliveryMapper.saveDtoToEntity(any())).willReturn(delivery);
        given(deliveryMapper.entityToDto(any())).willReturn(deliveryDto);
        DeliveryToSaveDto deliveryToSave = new DeliveryToSaveDto(orderDto, delivery.getAddress(), delivery.getCompany(), delivery.getWaybillNumber());
        DeliveryDto retuDto = deliveryService.saveDelivery(deliveryToSave);
        assertThat(retuDto).isNotNull();
    }

    @Test
    void testUpdateDelivery() {
        // Given a delivery, when update, return delivery
        DeliveryDto deliveryToUpdate = new DeliveryDto(1l, orderDto, "New Vegas", delivery.getCompany(), delivery.getWaybillNumber());
        Delivery deliveryUpdate = delivery;
        deliveryUpdate.setAddress("New Vegas");
        given(deliveryMapper.entityToDto(any())).willReturn(deliveryToUpdate);
        given(deliveryRepository.findById(any())).willReturn(Optional.of(delivery));
        given(deliveryRepository.save(any())).willReturn(deliveryUpdate);
        given(orderRepository.findById(any())).willReturn(Optional.of(order));
        DeliveryToSaveDto deliveryToSave = new DeliveryToSaveDto(orderDto, "New Vegas", delivery.getCompany(), delivery.getWaybillNumber());
        DeliveryDto result = deliveryService.updateDelivery(1l, deliveryToSave);
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1l);
        assertThat(result.address()).isEqualTo("New Vegas");
    }
}
