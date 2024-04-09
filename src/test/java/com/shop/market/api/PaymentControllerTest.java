package com.shop.market.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.market.Utils.PaymentMethod;
import com.shop.market.Utils.Status;
import com.shop.market.dto.client.ClientDto;
import com.shop.market.dto.order.OrderDto;
import com.shop.market.dto.payment.PaymentDto;
import com.shop.market.dto.payment.PaymentToSaveDto;
import com.shop.market.entities.Payment;
import com.shop.market.service.payment.PaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class PaymentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @Autowired
    private ObjectMapper objectMapper;
    private Payment payment;
    private PaymentDto paymentDto;

    ClientDto client1 = new ClientDto(Long.valueOf(1L), "Daniel Grandpa", "deathtothedevil@gmail.com", "The Hills");
    OrderDto testOrder = new OrderDto(Long.valueOf(1L), client1, LocalDateTime.now(), Status.PENDING);
    PaymentDto testPayment = new PaymentDto(Long.valueOf(1L), testOrder, 420.00, LocalDateTime.now(), PaymentMethod.PAYPAL);
    PaymentToSaveDto paymentToSaveDto = new PaymentToSaveDto(testOrder, 420.00, LocalDateTime.now(), PaymentMethod.PAYPAL);

    @Test
    void getPayments() throws Exception {
        given(paymentService.getAllPayments()).willReturn(List.of(testPayment));
        mockMvc.perform(get("/api/v1/payments")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    void getPaymentById() throws Exception {
        given(paymentService.findPaymentById(testPayment.id())).willReturn(testPayment);
        mockMvc.perform(get("/api/v1/payments/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void getPaymentByOrderId() throws Exception {
        given(paymentService.findByOrderIdAndPaymentMethod(testOrder.id(), PaymentMethod.PAYPAL)).willReturn(testPayment);
        mockMvc.perform(get("/api/v1/payments/order/1")
                .param("paymentMethod", PaymentMethod.PAYPAL.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void getPaymentsByTimeOfPaymentBetween() throws Exception {
        given(paymentService.findByTimeOfPaymentBetween(testPayment.timeOfPayment(), testPayment.timeOfPayment())).willReturn(List.of(testPayment));
        mockMvc.perform(get("/api/v1/payments/date-range")
                .param("startDate", testPayment.timeOfPayment().toString())
                .param("endDate", testPayment.timeOfPayment().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    void createNewPayment() throws Exception {
        given(paymentService.savePayment(any())).willReturn(testPayment);
        mockMvc.perform(post("/api/v1/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentToSaveDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void updatePayment() throws Exception {
        given(paymentService.updatePayment(testPayment.id(), paymentToSaveDto)).willReturn(testPayment);
        mockMvc.perform(put("/api/v1/payments/1")
                .content(objectMapper.writeValueAsString(paymentToSaveDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void deletePayment() throws Exception {
        willDoNothing().given(paymentService).deletePayment(testPayment.id());
        mockMvc.perform(delete("/api/v1/payments/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}