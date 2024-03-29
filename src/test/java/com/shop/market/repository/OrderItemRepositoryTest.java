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
import com.shop.market.entities.Payment;
import com.shop.market.entities.PaymentMethod;
import com.shop.market.entities.Product;

public class OrderItemRepositoryTest extends AbstractsIntegrationDBTest{
    ClientRepository clientRepository;
    OrderRepository orderRepository;
    ProductRepository productRepository;
    OrderItemRepository orderItemRepository;

    @Autowired
    public OrderItemRepositoryTest( ClientRepository clientRepository, OrderRepository orderRepository,
                                    ProductRepository productRepository, OrderItemRepository orderItemRepository) {
        this.clientRepository = clientRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
    }

    void initOrderItemTest(){
        List<Client> clients = TestUtil.genClients();
        List<Order> orders = TestUtil.genOrders(clients);
        List<Product> products = TestUtil.genProduct();
        List<OrderItem> orderItems = TestUtil.genOrderItems(orders, products);

        clientRepository.saveAllAndFlush(clients);
        orderRepository.saveAllAndFlush(orders);
        productRepository.saveAllAndFlush(products);
        orderItemRepository.saveAllAndFlush(orderItems);
    }

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        productRepository.deleteAll();
        orderItemRepository.deleteAll();
    }
    //create test
    @Test
    void givenAOrderItem_whenSave_thenOrderItemWithId(){
        List<Client> clients = TestUtil.genClients();
        List<Order> orders = TestUtil.genOrders(clients);
        List<Product> products = TestUtil.genProduct();

        OrderItem orderItem = OrderItem.builder()
                        .order(orders.get(0))
                        .product(products.get(0))
                        .amount(100)
                        .pricePerUnit(products.get(0).getPrice())
                        .build();
        
        clientRepository.saveAllAndFlush(clients);
        orderRepository.saveAllAndFlush(orders);
        productRepository.saveAllAndFlush(products);

        OrderItem orderItemSaved = orderItemRepository.save(orderItem);
        assertThat(orderItemSaved.getId()).isNotNull();
        assertThat(orderItemRepository.findAll()).hasSize(1);
    }

    //read test
    @Test
    void whenFindAll_thenGetAllOrderItems(){
        initOrderItemTest();
        List<OrderItem> orderItems = orderItemRepository.findAll();
        assertThat(orderItems).isNotEmpty();
        assertThat(orderItems).hasSize(6);
    }

    //Update test
    @Test
    void givenAOrderItemAndAmount_whenSetAmount_thenUpdateItemOrderedAmount(){
        initOrderItemTest();
        List<OrderItem> orderItems = orderItemRepository.findAll(); 
        OrderItem orderItem = orderItems.get(0);

        orderItem.setAmount(5);
        orderItemRepository.save(orderItem);

        Optional<OrderItem> updatedOrderItemOptional = orderItemRepository.findById(orderItem.getId());
        assertTrue(updatedOrderItemOptional.isPresent(), "OrderItem not found");

        OrderItem updatedOrderItem = updatedOrderItemOptional.get();
        assertEquals(orderItem.getAmount(), updatedOrderItem.getAmount(), "Amount not updated");
    }
    
    //Delete Test
    @Test
    void givenAOrderItem_whenDelete_thenDeleteOrderItem(){
        initOrderItemTest();
        OrderItem orderItem = orderItemRepository.findAll().get(4);
        Long size  = orderItemRepository.count();

        orderItemRepository.delete(orderItem);
        assertEquals(size-1, orderItemRepository.count());
    }

    @Test
    void givenAnOrder_whenFindByOrderId_thenGetOrderItemsInOrder(){
        initOrderItemTest();
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderRepository.findAll().get(0).getId());
        assertThat(orderItems).isNotEmpty();
        assertThat(orderItems).hasSize(2);
    }

    @Test
    void givenAProduct_whenFindByProduct_thenGetOrderItemsWithProduct(){
        initOrderItemTest();
        List<OrderItem> orderItems = orderItemRepository.findByProduct(productRepository.findAll().get(1));
        assertThat(orderItems).isNotEmpty();
        assertThat(orderItems).hasSize(2);
    }

    @Test
    void givenAProduct_whenGetProductSales_thenSumProductSales(){
        initOrderItemTest();
        Product product = productRepository.findAll().get(0);
        Double sales = orderItemRepository.getProductSales(product);
        assertEquals(140*product.getPrice(),sales);
    }
}
