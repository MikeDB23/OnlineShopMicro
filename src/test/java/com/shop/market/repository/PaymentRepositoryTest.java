package com.shop.market.repository;

import static org.assertj.core.api.Assertions.assertThat;
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
import com.shop.market.Utils.PaymentMethod;
import com.shop.market.entities.Client;
import com.shop.market.entities.Order;
import com.shop.market.entities.Payment;

public class PaymentRepositoryTest extends AbstractsIntegrationDBTest{
    ClientRepository clientRepository;
    OrderRepository orderRepository;
    PaymentRepository paymentRepository;
    

    @Autowired
    public PaymentRepositoryTest(ClientRepository clientRepository, OrderRepository orderRepository, PaymentRepository paymentRepository) {
        this.clientRepository = clientRepository;
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
    }


    void initPaymentTest(){
        List<Client> clients = TestUtil.genClients();
        List<Order> orders = TestUtil.genOrders(clients);
        List<Payment> payments = TestUtil.genPayments(orders);

        clientRepository.saveAllAndFlush(clients);
        orderRepository.saveAllAndFlush(orders);
        paymentRepository.saveAllAndFlush(payments);
    }

    @BeforeEach
    void setUp() {
        clientRepository.deleteAll();
        orderRepository.deleteAll();
        paymentRepository.deleteAll();
    }
    //create test
    @Test
    void givenAPayment_whenSave_thenPaymentWithId(){
    
        List<Client> clients = TestUtil.genClients();
        List<Order> orders = TestUtil.genOrders(clients);
        Payment payment = Payment.builder()
                        .order(orders.get(0))
                        .totalPayment(1000.24)
                        .timeOfPayment(LocalDateTime.now())
                        .paymentMethod(PaymentMethod.PSE)
                        .build();
        
        clientRepository.saveAll(clients);
        orderRepository.saveAll(orders);

        Payment paymentSaved = paymentRepository.save(payment);
        assertThat(paymentSaved.getId()).isNotNull();
        assertThat(paymentRepository.findAll()).hasSize(1);
    }

    //read test
    @Test
    void whenFindAll_thenGetAllPayments(){
        initPaymentTest();
        List<Payment> payments = paymentRepository.findAll();
        assertThat(payments).isNotEmpty();
        assertThat(payments).hasSize(5);
    }

    //Update test
    @Test
    void givenAPaymentAndPaymentMethod_whenSetPaymentMethod_thenUpdatePaymentMethodUsed(){
        initPaymentTest();
        List<Payment> payments = paymentRepository.findAll(); 
        Payment payment = payments.get(0);

        payment.setPaymentMethod(PaymentMethod.DAVIPLATA);
        paymentRepository.save(payment);

        Optional<Payment> updatedPaymentOptional = paymentRepository.findById(payment.getId());
        assertTrue(updatedPaymentOptional.isPresent(), "Payment not found");

        Payment updatedPayment = updatedPaymentOptional.get();
        assertEquals(payment.getPaymentMethod(), updatedPayment.getPaymentMethod(), "Payment Method not updated");
    }
    
    //Delete Test
    @Test
    void givenAPayment_whenDelete_thenDeletePayment(){
        initPaymentTest();
        Payment payment = paymentRepository.findAll().get(4);
        Long size  = paymentRepository.count();

        paymentRepository.delete(payment);
        assertEquals(size-1, paymentRepository.count());
    }

    @Test
    void givenTwoLocalDateTimes_whenFindByTimeOfPaymentBetween_thenGetPaymentsMadeBewtweenTheDates(){
        initPaymentTest();
        List<Payment> payments = paymentRepository.findByTimeOfPaymentBetween(  LocalDateTime.of(2021,6,1,0,0), 
                                                                                LocalDateTime.now());
        assertThat(payments).isNotEmpty();
        assertThat(payments).hasSize(2);
    }

    @Test
    void givenAnOrderAndPaymentMethod_whenFindByOrderIdAndPaymentMethod_thenGetPaymentByOrdersPaidWithMethodProvided(){
        initPaymentTest();
        Order order = orderRepository.findAll().get(1);
        Optional<Payment> paymentOptional = paymentRepository.findByOrderIdAndPaymentMethod(order.getId(),
                                                                                            PaymentMethod.PSE);
        assertThat(paymentOptional).isNotEmpty();
        Payment payment = paymentOptional.get();
        assertEquals(payment.getPaymentMethod(), PaymentMethod.PSE);
    }
}
