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
import com.shop.market.dto.order.OrderDto;
import com.shop.market.dto.order.OrderMapper;
import com.shop.market.dto.payment.PaymentDto;
import com.shop.market.dto.payment.PaymentMapper;
import com.shop.market.dto.payment.PaymentToSaveDto;
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
    ClientDto clientDto = new ClientDto(clients.get(0).getId(), clients.get(0).getName(), clients.get(0).getEmail(), clients.get(0).getAddress());
    List<Order> orders = TestUtil.genOrders(clients);
    Order order = orders.get(0);
    OrderDto orderDto = new OrderDto(order.getId(), clientDto, order.getTimeOfOrder(), order.getStatus());
    List<Payment> payments = TestUtil.genPayments(orders);
    Payment payment = payments.get(0);
    PaymentDto paymentDto = new PaymentDto(1l, orderDto, payment.getTotalPayment(), payment.getTimeOfPayment(), payment.getPaymentMethod());
    List<Payment> resuList = new ArrayList<>();

    @BeforeEach
    void setUp(){
        order.setId(1l);
        payment.setId(1l);
        resuList.clear();
        resuList.add(payment);
    }

    @Test
    void testDeletePayment() {
        // Given a payment, when delete, do nothing
        willDoNothing().given(paymentRepository).delete(any());
        given(paymentRepository.findById(payment.getId())).willReturn(Optional.of(payment));
        paymentService.deletePayment(payment.getId());
        verify(paymentRepository, times(1)).delete(any());
    }

    @Test
    void testFindByOrderIdAndPaymentMethod() {
        // Given an order id and a payment method, when find by order id and method, return payment
        given(paymentRepository.findByOrderIdAndPaymentMethod(order.getId(), payment.getPaymentMethod())).willReturn(Optional.of(payment));
        given(paymentMapper.entityToDto(any())).willReturn(paymentDto);
        PaymentDto retuDto = paymentService.findByOrderIdAndPaymentMethod(order.getId(), payment.getPaymentMethod());
        assertThat(retuDto).isNotNull();
    }

    @Test
    void testFindByTimeOfPaymentBetween() {
        // Given two times, when find between, return payment
        given(paymentRepository.findByTimeOfPaymentBetween(payment.getTimeOfPayment(), payment.getTimeOfPayment())).willReturn(resuList);
        List<PaymentDto> retuList = paymentService.findByTimeOfPaymentBetween(payment.getTimeOfPayment(), payment.getTimeOfPayment());
        assertThat(retuList).isNotNull();
        assertThat(retuList).asList().hasSize(1);
    }

    @Test
    void testFindPaymentById() {
        // Given an Id, when find by Id, return payment
        given(paymentRepository.findById(payment.getId())).willReturn(Optional.of(payment));
        given(paymentMapper.entityToDto(any())).willReturn(paymentDto);
        PaymentDto retuDto = paymentService.findPaymentById(1l);
        assertThat(retuDto).isNotNull();
        assertThat(retuDto.id()).isEqualTo(1l);
    }

    @Test
    void testGetAllPayments() {
        // When find all payments, return payments
        given(paymentRepository.findAll()).willReturn(payments);
        List<PaymentDto> retuList = paymentService.getAllPayments();
        assertThat(retuList).isNotNull();
        assertThat(retuList).asList().hasSize(5);
    }

    @Test
    void testSavePayment() {
        // Given a payment, when save, return payment
        given(paymentMapper.saveDtoToEntity(any())).willReturn(payment);
        given(paymentMapper.entityToDto(any())).willReturn(paymentDto);
        PaymentToSaveDto paymentToSaveDto = new PaymentToSaveDto(orderDto, payment.getTotalPayment(), payment.getTimeOfPayment(), payment.getPaymentMethod());
        PaymentDto retuPaymentDto = paymentService.savePayment(paymentToSaveDto);
        assertThat(retuPaymentDto).isNotNull();
        assertThat(retuPaymentDto.id()).isEqualTo(1l);
    }

    @Test
    void testUpdatePayment() {
        // Given a payment, when update, return payment
        PaymentDto paymentToUpdate = new PaymentDto(1l, orderDto, 6000.00, payment.getTimeOfPayment(), payment.getPaymentMethod());
        Payment updatedPayment = payment;
        updatedPayment.setTotalPayment(6000.00);
        given(paymentMapper.entityToDto(any())).willReturn(paymentToUpdate);
        given(paymentRepository.findById(any())).willReturn(Optional.of(payment));
        given(paymentRepository.save(any())).willReturn(updatedPayment);
        given(orderRepository.findById(any())).willReturn(Optional.of(order));
        PaymentToSaveDto paymentToSaveDto = new PaymentToSaveDto(orderDto, 6000.00, payment.getTimeOfPayment(), payment.getPaymentMethod());
        PaymentDto retuPaymentDto = paymentService.updatePayment(1l, paymentToSaveDto);
        assertThat(retuPaymentDto).isNotNull();
        assertThat(retuPaymentDto.id()).isEqualTo(1l);
        assertThat(retuPaymentDto.totalPayment()).isEqualTo(6000.00);
    }
}
