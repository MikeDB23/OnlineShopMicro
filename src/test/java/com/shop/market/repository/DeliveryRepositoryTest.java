package com.shop.market.repository;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import com.shop.market.AbstractsIntegrationDBTest;

public class DeliveryRepositoryTest extends AbstractsIntegrationDBTest{
    ProductRepository productRepository;
    OrderRepository orderRepository;
    DeliveryRepository deliveryRepository;

    @Autowired
    public DeliveryRepositoryTest(ProductRepository productRepository, OrderRepository orderRepository,
            DeliveryRepository deliveryRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.deliveryRepository = deliveryRepository;
    }

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        productRepository.deleteAll();
        deliveryRepository.deleteAll();
    }
    
}
