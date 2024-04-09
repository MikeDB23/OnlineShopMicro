package com.shop.market.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.market.Utils.Status;
import com.shop.market.dto.client.ClientDto;
import com.shop.market.dto.client.ClientMapper;
import com.shop.market.dto.order.OrderDto;
import com.shop.market.dto.order.OrderToSaveDto;
import com.shop.market.dto.orderItem.OrderItemDto;
import com.shop.market.dto.product.ProductDto;
import com.shop.market.dto.product.ProductMapper;
import com.shop.market.entities.Client;
import com.shop.market.entities.Order;
import com.shop.market.entities.Product;
import com.shop.market.service.order.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class OrderControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;
    private Order order;
    private OrderDto orderDto;


    ClientDto client1 = new ClientDto(Long.valueOf(1L), "Daniel Grandpa", "deathtothedevil@gmail.com", "The Hills");
    OrderDto testOrder = new OrderDto(Long.valueOf(1L), client1, LocalDateTime.now(), Status.PENDING);
    OrderToSaveDto orderToSaveDto = new OrderToSaveDto(client1, LocalDateTime.now(), Status.PENDING);
    ProductDto productDto1 = new ProductDto(Long.valueOf(1L), "Mondongo", 6900.00, 15);

    @BeforeEach
    void setUp(){

    }

    @Test
    void getOrders() throws Exception {
        List<OrderDto> productDtoList = List.of(testOrder, new OrderDto(Long.valueOf(2L), client1, LocalDateTime.now(), Status.PENDING));
        given(orderService.getAllOrders()).willReturn(productDtoList);
        mockMvc.perform(get("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));
    }

    @Test
    void getOrderById() throws Exception {
        given(orderService.findOrderById(testOrder.id())).willReturn(testOrder);
        mockMvc.perform(get("/api/v1/orders/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void getOrderItemsByClientId() throws Exception {
        List<OrderItemDto> productDtoList = List.of(new OrderItemDto(Long.valueOf(1L), orderDto, productDto1, 3, 6900.00));
        given(orderService.findByItemsPerOrder(testOrder.id())).willReturn(productDtoList);
        mockMvc.perform(get("/api/v1/orders/customer/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    void getOrdersByTimeOfOrderBetween() throws Exception {
        given(orderService.findByTimeOfOrderBetween(testOrder.timeOfOrder(), testOrder.timeOfOrder())).willReturn(List.of(testOrder));
        mockMvc.perform(get("/api/v1/orders/date-range")
                        .param("startDate", testOrder.timeOfOrder().toString())
                        .param("endDate", testOrder.timeOfOrder().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    void createNewOrder() throws Exception {
        OrderToSaveDto orderToSaveDto1 = new OrderToSaveDto(client1, null, testOrder.status());
        String json = new ObjectMapper().writeValueAsString(orderToSaveDto1);
        given(orderService.saveOrder(any())).willReturn(testOrder);
        mockMvc.perform(post("/api/v1/orders")
                        .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void updateOrder() throws Exception {
        OrderToSaveDto orderToSaveDto1 = new OrderToSaveDto(client1, null, testOrder.status());
        given(orderService.updateOrder(testOrder.id(), orderToSaveDto1)).willReturn(testOrder);
        mockMvc.perform(put("/api/v1/orders/1")
                .content(objectMapper.writeValueAsString(orderToSaveDto1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void deleteOrder() throws Exception {
        willDoNothing().given(orderService).deleteOrder(testOrder.id());
        mockMvc.perform(delete("/api/v1/orders/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}