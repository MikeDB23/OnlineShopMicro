package com.shop.market.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.shop.market.AbstractsIntegrationDBTest;
import com.shop.market.TestUtil;
import com.shop.market.entities.Client;
import com.shop.market.entities.Order;
import com.shop.market.entities.OrderItem;
import com.shop.market.entities.Status;

public class OrderRepositoryTest extends AbstractsIntegrationDBTest{
    ClientRepository clientRepository;
    ProductRepository productRepository;
    OrderRepository orderRepository;
    OrderItemRepository orderItemRepository;

    @Autowired
    public OrderRepositoryTest(ClientRepository clientRepository,ProductRepository productRepository , OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    void initOrderTest(){
        List<Client> clients = TestUtil.genClients();
        List<Order> orders = TestUtil.genOrders(clients);
        
        clientRepository.saveAll(clients);
        clientRepository.flush();

        orderRepository.saveAll(orders);
        orderRepository.flush();
    }

    @BeforeEach
    void setUp() {
        clientRepository.deleteAll();
        productRepository.deleteAll();
        orderRepository.deleteAll();
        orderItemRepository.deleteAll();
    }

     //create test
    @Test
    void givenAnOrder_whenSave_thenOrderWithId(){
        List<Client> clients = TestUtil.genClients();
        Order order = Order.builder()
                            .client(clients.get(2))
                            .timeOfOrder(LocalDateTime.of(2021, 4, 10, 22, 30))
                            .status(Status.DELIVERED)
                            .build();
        
        clientRepository.saveAll(clients);
        Order orderSaved = orderRepository.save(order);
        assertThat(orderSaved.getId()).isNotNull();
        assertThat(orderRepository.findAll()).hasSize(1);
    }

    //read test
    @Test
    void whenFindAll_thenGetAllOrders(){
        initOrderTest();
        List<Order> orders = orderRepository.findAll();
        assertThat(orders).isNotEmpty();
        assertThat(orders).hasSize(7);
    }

    //Update test
    @Test
    void givenAnOrderAndStatus_whenSetStatus_thenUpdateOrderStatus(){
        initOrderTest();
        List<Order> orders = orderRepository.findAll(); 
        Order order = orders.get(0);

        order.setStatus(Status.SENT);
        orderRepository.save(order);

        Optional<Order> updatedOrderOptional = orderRepository.findById(order.getId());
        assertTrue(updatedOrderOptional.isPresent(), "Order not found");

        Order updatedOrder = updatedOrderOptional.get();
        assertEquals(order.getStatus(), updatedOrder.getStatus(), "Status not updated");
    }
    
    //Delete Test
    @Test
    void givenAOrder_whenDelete_thenDeleteOrder(){
        initOrderTest();
        Order order = orderRepository.findAll().get(4);
        Long size  = orderRepository.count();

        orderRepository.delete(order);
        assertEquals(size-1, orderRepository.count());
    }

    @Test
    void givenTwoLocalDateTimes_whenFindByTimeOfOrderBetween_thenGetOrdersBetweenPeriod(){
        initOrderTest();
        LocalDateTime initDate = LocalDateTime.of(2020, 1, 1, 0, 0);
        List<Order> orders = orderRepository.findByTimeOfOrderBetween(initDate, LocalDateTime.now());
        
        assertThat(orders).hasSize(5);
    }

    @Test
    void givenAClientAndStatus_whenFindByClientAndStatus_thenGetOrdersFromClientWithTheStatusProvided(){
        initOrderTest();
        List<Order> orders = orderRepository.findByClientAndStatus(clientRepository.findAll().get(0), Status.PENDING);
        assertThat(orders).hasSize(2);
    }

    @Test
    void givenAClient_whenFindByItemsPerOrder_thenGetItemsOrderedByClient(){
        initOrderTest();
        productRepository.saveAll(TestUtil.genProduct());
        orderItemRepository.saveAll(TestUtil.genOrderItems(orderRepository.findAll(), productRepository.findAll()));
        List<OrderItem> items = orderRepository.findByItemsPerOrder(clientRepository.findAll().get(0));
        System.out.println(items.get(0));
        assertThat(items).isNotEmpty();
        assertThat(items).hasSize(5);
    }   
}
