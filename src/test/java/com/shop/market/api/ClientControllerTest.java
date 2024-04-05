package com.shop.market.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;

import com.shop.market.dto.client.ClientToSaveDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.market.TestUtil;
import com.shop.market.dto.client.ClientDto;
import com.shop.market.entities.Client;
import com.shop.market.service.client.ClientService;

import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    ClientDto client1 = new ClientDto(Long.valueOf(1L), "Daniel Grandpa", "deathtothedevil@gmail.com", "The Hills");
    ClientToSaveDto clientToSaveDto = new ClientToSaveDto("Daniel Grandpa", "deathtothedevil@gmail.com", "The Hills");

    @BeforeEach
    void setUp(){

    }

    @Test
    void testCreateNewClient() throws Exception{
        given(clientService.saveClient(any())).willReturn(client1);
        String json = new ObjectMapper().writeValueAsString(clientToSaveDto);
        mockMvc.perform(post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteClient() throws Exception{
        willDoNothing().given(clientService).deleteClient(client1.id());
        mockMvc.perform(delete("/api/v1/customers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetClientByAddress() throws Exception{
        given(clientService.findByAddress(client1.address())).willReturn(List.of(client1));
        mockMvc.perform(get("/api/v1/customers/city")
                .contentType(MediaType.APPLICATION_JSON)
                        .param("address", client1.address()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    void testGetClientByEmail() throws Exception {
        given(clientService.findByEmail(client1.email())).willReturn(client1);
        mockMvc.perform(get("/api/v1/customers/email/deathtothedevil@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void testGetClientById() throws Exception{
        given(clientService.findClientById(client1.id())).willReturn(client1);
        mockMvc.perform(get("/api/v1/customers/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void testGetClients() throws Exception{
        ClientDto client1 = new ClientDto(Long.valueOf(1L), "Why", "test@pain.com", "Heck");
        List<ClientDto> clientDtos = List.of(client1, new ClientDto(Long.valueOf(2L), "Suffering", "imsofakingdone@gmail.com", "My house"));
        given(clientService.getAllClients()).willReturn(clientDtos);

        mockMvc.perform(get("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));
    }

    @Test
    void testUpdateClient() throws Exception{
        given(clientService.updateClient(client1.id(), clientToSaveDto)).willReturn(client1);
        String json = new ObjectMapper().writeValueAsString(clientToSaveDto);
        mockMvc.perform(put("/api/v1/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }
}
