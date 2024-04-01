package com.shop.market.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.shop.market.AbstractsIntegrationDBTest;
import com.shop.market.TestUtil;
import com.shop.market.Utils.Status;
import com.shop.market.entities.Client;
import com.shop.market.entities.Delivery;
import com.shop.market.entities.Order;
import com.shop.market.entities.Product;

public class DeliveryRepositoryTest extends AbstractsIntegrationDBTest{
    ClientRepository clientRepository;
    ProductRepository productRepository;
    OrderRepository orderRepository;
    DeliveryRepository deliveryRepository;

    @Autowired
    public DeliveryRepositoryTest(  ClientRepository clientRepository, ProductRepository productRepository,
                                    OrderRepository orderRepository, DeliveryRepository deliveryRepository) {
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.deliveryRepository = deliveryRepository;
    }

    void initDeliveryTest(){
        List<Client> clients = TestUtil.genClients();
        List<Order> orders = TestUtil.genOrders(clients);
        List<Product> products = TestUtil.genProduct();
        List<Delivery> deliveries = TestUtil.genDeliveries(orders);

        clientRepository.saveAllAndFlush(clients);
        orderRepository.saveAllAndFlush(orders);
        productRepository.saveAllAndFlush(products);
        deliveryRepository.saveAllAndFlush(deliveries);
    }

    @BeforeEach
    void setUp() {
        clientRepository.deleteAll();
        orderRepository.deleteAll();
        productRepository.deleteAll();
        deliveryRepository.deleteAll();
    }

    //create test
    @Test
    void givenADelivery_whenSave_thenDeliveryWithId(){
        List<Client> clients = TestUtil.genClients();
        List<Order> orders = TestUtil.genOrders(clients);
        List<Product> products = TestUtil.genProduct();

        Delivery delivery = Delivery.builder()
                        .order(orders.get(0))
                        .address(orders.get(0).getClient().getAddress())
                        .company("Mango delivery")
                        .waybillNumber(1)
                        .build();

        
        clientRepository.saveAllAndFlush(clients);
        productRepository.saveAllAndFlush(products);
        orderRepository.saveAllAndFlush(orders);
        
        Delivery deliverySaved = deliveryRepository.save(delivery);

        assertThat(deliverySaved.getId()).isNotNull();
        assertThat(deliveryRepository.findAll()).hasSize(1);
    }

    //read test
    @Test
    void whenFindAll_thenGetAllDeliveries(){
        initDeliveryTest();
        List<Delivery> deliveries = deliveryRepository.findAll();
        assertThat(deliveries).isNotEmpty();
        assertThat(deliveries).hasSize(7);
    }

    //Update test
    @Test
    void givenADeliveryAndCompany_whenSetCompany_thenUpdateDeliveryCompany(){
        initDeliveryTest();
        List<Delivery> deliveries = deliveryRepository.findAll(); 
        Delivery delivery = deliveries.get(0);

        delivery.setCompany("iFast delivery");
        deliveryRepository.save(delivery);

        Optional<Delivery> updatedDeliveryOptional = deliveryRepository.findById(delivery.getId());
        assertTrue(updatedDeliveryOptional.isPresent(), "Delivery not found");

        Delivery updatedDelivery = updatedDeliveryOptional.get();
        assertEquals(delivery.getCompany(), updatedDelivery.getCompany(), "Delivery not updated");
    }
    
    //Delete Test
    @Test
    void givenAOrderItem_whenDelete_thenDeleteOrderItem(){
        initDeliveryTest();
        Delivery delivery = deliveryRepository.findAll().get(2);
        Long size  = deliveryRepository.count();

        deliveryRepository.delete(delivery);
        assertEquals(size-1, deliveryRepository.count());
    }
    
    @Test
    void givenAnOrder_whenFindByOrder_thenGetOrderDeliveryData(){
        initDeliveryTest();
        Order order = orderRepository.findAll().get(5);
        Optional<Delivery> deliveryOptional = deliveryRepository.findByOrderId(order.getId());
        assertThat(deliveryOptional).isNotEmpty();
        Delivery delivery = deliveryOptional.get();
        assertEquals(order.getClient().getAddress(), delivery.getAddress());
    }

    @Test
    void givenACompany_whenFindByCompany_thenGetCompanyDeliveries(){
        initDeliveryTest();
        String company = "Coordinadora";
        List<Delivery> deliveries = deliveryRepository.findByCompany(company);
        assertThat(deliveries).isNotEmpty();
        assertEquals(100000+deliveryRepository.findAll().size(), deliveries.get(deliveries.size()-1).getWaybillNumber());
    }

    @Test
    void givenAStatus_whenFindByOrderStatus_thenGetDeliveriesWithOrderStatusSpecified(){
        initDeliveryTest();
        Status status = Status.DELIVERED;
        List<Delivery> deliveries = deliveryRepository.findByOrderStatus(status);
        assertThat(deliveries).isNotEmpty();
        assertThat(deliveries).hasSize(3);
    }
}
