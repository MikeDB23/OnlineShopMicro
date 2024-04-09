package com.shop.market.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.market.Utils.Status;
import com.shop.market.dto.client.ClientDto;
import com.shop.market.dto.order.OrderDto;
import com.shop.market.dto.orderItem.OrderItemDto;
import com.shop.market.dto.orderItem.OrderItemToSaveDto;
import com.shop.market.dto.product.ProductDto;
import com.shop.market.entities.OrderItem;
import com.shop.market.service.orderItem.OrderItemService;
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
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class OrderItemControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private OrderItemService orderItemService;

    @Autowired
    private ObjectMapper objectMapper;
    private OrderItem orderItem;
    private OrderItemDto orderItemDto;

    ClientDto client1 = new ClientDto(Long.valueOf(1L), "Daniel Grandpa", "deathtothedevil@gmail.com", "The Hills");
    OrderDto testOrder = new OrderDto(Long.valueOf(1L), client1, LocalDateTime.now(), Status.PENDING);
    ProductDto testProduct = new ProductDto(Long.valueOf(1L), "Mondongo", 6900.00, 15);
    OrderItemDto testItem = new OrderItemDto(Long.valueOf(1L), testOrder, testProduct, 6, 6900.00);
    OrderItemToSaveDto itemToSaveDto = new OrderItemToSaveDto(testOrder, testProduct, 6, 6900.00);

    @Test
    void getOrdersItems() throws Exception {
        given(orderItemService.getAllOrderItems()).willReturn(List.of(testItem));
        mockMvc.perform(get("/api/v1/order-items")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    void getOrderItemById() throws Exception {
        given(orderItemService.findOrderItemById(testItem.id())).willReturn(testItem);
        mockMvc.perform(get("/api/v1/order-items/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void getOrderItemsByOrder() throws Exception {
        given(orderItemService.findByOrderId(testOrder.id())).willReturn(List.of(testItem));
        mockMvc.perform(get("/api/v1/order-items/order/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    void getOrderItemsByProduct() throws Exception {
        given(orderItemService.findByProductId(testProduct.id())).willReturn(List.of(testItem));
        mockMvc.perform(get("/api/v1/order-items/product/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    void createNewOrderItem()throws Exception {
        given(orderItemService.saveOrderItem(itemToSaveDto)).willReturn(testItem);
        mockMvc.perform(post("/api/v1/order-items")
                .content(objectMapper.writeValueAsString(itemToSaveDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void updateOrderItem() throws Exception {
        given(orderItemService.updateOrderItem(testItem.id(), itemToSaveDto)).willReturn(testItem);
        mockMvc.perform(put("/api/v1/order-items/1")
                .content(objectMapper.writeValueAsString(itemToSaveDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void deleteOrder() throws Exception {
        willDoNothing().given(orderItemService).deleteOrderItem(testItem.id());
        mockMvc.perform(delete("/api/v1/order-items/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}