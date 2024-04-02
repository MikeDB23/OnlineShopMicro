package com.shop.market.service;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.shop.market.TestUtil;
import com.shop.market.dto.client.ClientMapper;
import com.shop.market.dto.order.OrderMapper;
import com.shop.market.dto.payment.PaymentMapper;
import com.shop.market.entities.Client;
import com.shop.market.entities.Order;
import com.shop.market.entities.Payment;
import com.shop.market.repository.ClientRepository;
import com.shop.market.repository.OrderRepository;
import com.shop.market.repository.PaymentRepository;
import com.shop.market.service.payment.PaymentServiceImpl;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentMapper paymentMapper;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    List<Client> clients = TestUtil.genClients();
    List<Order> orders = TestUtil.genOrders(clients);
    List<Payment> payments = TestUtil.genPayments(orders);

    @Test
    void testDeletePayment() {

    }

    @Test
    void testFindByOrderIdAndPaymentMethod() {

    }

    @Test
    void testFindByTimeOfPaymentBetween() {

    }

    @Test
    void testFindPaymentById() {

    }

    @Test
    void testGetAllPayments() {

    }

    @Test
    void testSavePayment() {

    }

    @Test
    void testUpdatePayment() {

    }
}
