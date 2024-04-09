package com.shop.market.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.market.dto.client.ClientDto;
import com.shop.market.dto.product.ProductDto;
import com.shop.market.dto.product.ProductToSaveDto;
import com.shop.market.entities.Client;
import com.shop.market.entities.Product;
import com.shop.market.service.product.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class ProductControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;
    private Product product;
    private ProductDto productDto;

    ProductDto testProduct = new ProductDto(Long.valueOf(1L), "Mondongo", 6900.00, 15);
    ProductToSaveDto productToSaveDto = new ProductToSaveDto("Mondongo", 6900.00, 15);

    @Test
    void getProducts() throws Exception{
        List<ProductDto> productDtoList = List.of(testProduct, new ProductDto(Long.valueOf(2L), "Yuca", 8000.00, 6));
        given(productService.getAllProducts()).willReturn(productDtoList);
        mockMvc.perform(get("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));
    }

    @Test
    void getProductById() throws Exception{
        given(productService.findProductById(testProduct.id())).willReturn(testProduct);
        mockMvc.perform(get("/api/v1/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void getProductBySearchParam() throws Exception {
        given(productService.findByNameLike(any())).willReturn(List.of(testProduct));
        mockMvc.perform(get("/api/v1/products/search")
                .contentType(MediaType.APPLICATION_JSON)
                        .param("searchTerm", "Mon"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    void getProductInStock() throws Exception{
        given(productService.findByStockGreaterThanZero()).willReturn(List.of(testProduct));
        mockMvc.perform(get("/api/v1/products/instock")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    void createNewProduct() throws Exception{
        given(productService.saveProduct(productToSaveDto)).willReturn(testProduct);
        String json = new ObjectMapper().writeValueAsString(productToSaveDto);
        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void updateProduct() throws Exception{
        given(productService.updateProduct(testProduct.id(), productToSaveDto)).willReturn(testProduct);
        String json = new ObjectMapper().writeValueAsString(productToSaveDto);
        mockMvc.perform(put("/api/v1/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void deleteProduct() throws Exception{
        willDoNothing().given(productService).deleteProduct(testProduct.id());
        mockMvc.perform(delete("/api/v1/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}