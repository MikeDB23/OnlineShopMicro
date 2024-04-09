package com.shop.market.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.market.Utils.Status;
import com.shop.market.dto.client.ClientDto;
import com.shop.market.dto.delivery.DeliveryDto;
import com.shop.market.dto.delivery.DeliveryToSaveDto;
import com.shop.market.dto.order.OrderDto;
import com.shop.market.dto.order.OrderToSaveDto;
import com.shop.market.entities.Delivery;
import com.shop.market.service.delivery.DeliveryService;
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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class DeliveryControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private DeliveryService deliveryService;

    @Autowired
    private ObjectMapper objectMapper;
    private Delivery delivery;
    private DeliveryDto deliveryDto;

    ClientDto client1 = new ClientDto(Long.valueOf(1L), "Daniel Grandpa", "deathtothedevil@gmail.com", "The Hills");
    OrderDto testOrder = new OrderDto(Long.valueOf(1L), client1, LocalDateTime.now(), Status.PENDING);
    DeliveryDto testDelivery = new DeliveryDto(Long.valueOf(1L), testOrder, "TheHills", "Coordinadora", 69);
    DeliveryToSaveDto deliveryToSaveDto = new DeliveryToSaveDto(testOrder, "TheHills", "Coordinadora", 69);

    @Test
    void getDeliveries() throws Exception {
        given(deliveryService.getAllDeliveries()).willReturn(List.of(testDelivery));
        mockMvc.perform(get("/api/v1/shipping")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    void getDeliveryById() throws Exception {
        given(deliveryService.findDeliveryById(testDelivery.id())).willReturn(testDelivery);
        mockMvc.perform(get("/api/v1/shipping/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void getDeliveryByOrderId() throws Exception {
        given(deliveryService.findByOrderId(testOrder.id())).willReturn(testDelivery);
        mockMvc.perform(get("/api/v1/shipping/order/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void getDeliveryByCompany() throws Exception {
        given(deliveryService.findByCompany(testDelivery.company())).willReturn(List.of(testDelivery));
        mockMvc.perform(get("/api/v1/shipping/carrier")
                        .param("name", "Coordinadora")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    void createNewDelivery() throws Exception {
        given(deliveryService.saveDelivery(any())).willReturn(testDelivery);
        mockMvc.perform(post("/api/v1/shipping")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(deliveryToSaveDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void updateDelivery() throws Exception {
        given(deliveryService.updateDelivery(testDelivery.id(), deliveryToSaveDto)).willReturn(testDelivery);
        mockMvc.perform(put("/api/v1/shipping/1")
                .content(objectMapper.writeValueAsString(deliveryToSaveDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void deleteDelivery() throws Exception {
        willDoNothing().given(deliveryService).deleteDelivery(testDelivery.id());
        mockMvc.perform(delete("/api/v1/shipping/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}