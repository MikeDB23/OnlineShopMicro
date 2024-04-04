package com.shop.market.controller;

import static org.mockito.BDDMockito.given;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.market.TestUtil;
import com.shop.market.dto.client.ClientDto;
import com.shop.market.entities.Client;
import com.shop.market.service.client.ClientService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.CoreMatchers.is;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class ClientControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    @Autowired
    private ObjectMapper objectMapper;
    private Client client;
    private ClientDto clientDto;

    List<Client> clients = TestUtil.genClients();
    Client testClient = clients.get(0);

    @BeforeEach
    void setUp(){

    }

    @Test
    void testCreateNewClient() {
        
    }

    @Test
    void testDeleteClient() {

    }

    @Test
    void testGetClientByAddress() {

    }

    @Test
    void testGetClientByEmail() {

    }

    @Test
    void testGetClientById() {

    }

    @Test
    void testGetClients() throws Exception{
        ClientDto client1 = new ClientDto(1L, "Why", "test@pain.com", "Heck");
        List<ClientDto> clientDtos = List.of(client1, new ClientDto(Long.valueOf(2L), "Suffering", "imsofakingdone@gmail.com", "My house"));
        given(clientService.getAllClients()).willReturn(clientDtos);

        mockMvc.perform(get("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    void testUpdateClient() {

    }
}
