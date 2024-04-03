package com.shop.market.service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.shop.market.TestUtil;
import com.shop.market.dto.client.ClientDto;
import com.shop.market.dto.client.ClientMapper;
import com.shop.market.dto.client.ClientToSaveDto;
import com.shop.market.entities.Client;
import com.shop.market.repository.ClientRepository;
import com.shop.market.service.client.ClientServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    @InjectMocks
    private ClientServiceImpl clientService;

    List<Client> clients = TestUtil.genClients();
    Client client = clients.get(0);
    ClientDto clientDto2 = new ClientDto(1l, "Gian Carlos Astori Goose", "duck_destroyer89@gmail.com", "Unimag Lake");
    List<Client> answerClients = new ArrayList<>();

    @BeforeEach
    void setUp(){
        // client = Client.builder()
        //         .id(Long.valueOf(69))
        //         .name("Daniel Grandpa Gorge")
        //         .email("deathtothedevil@gmail.com")
        //         .address("The Hills")
        //         .build();
        // clients.add(client); 
        client.setId(1l);
        answerClients.clear();
        answerClients.add(client);
    }

    @Test
    void testDeleteClient() {
        //Given a client, when delete, do nothing
        Long clientID = 1l;
        willDoNothing().given(clientRepository).delete(any());
        given(clientRepository.findById(clientID)).willReturn(Optional.of(client));
        clientService.deleteClient(clientID);

        verify(clientRepository, times(1)).delete(any());
    }

    @Test
    void testFindByAddress() {
        // Given an address, when find by address, return client
        String address = "Unimag Lake";
        given(clientRepository.findByAddress(address)).willReturn(answerClients);

        List<ClientDto> clientDto = clientService.findByAddress(address);

        assertThat(clientDto).isNotNull();
    }

    @Test
    void testFindByEmail() {
        // Given an email, when find by email, return client
        String emailToSearch = client.getEmail();
        given(clientRepository.findByEmail(emailToSearch)).willReturn(Optional.of(client));
        given(clientMapper.entityToDto(any())).willReturn(clientDto2);
        ClientDto retuValue = clientService.findByEmail(emailToSearch);
        System.out.println("---------------------");
        System.out.println(emailToSearch);
        System.out.println(retuValue);
        assertThat(retuValue).isNotNull();
    }

    @Test
    void testFindByNameStartingWith() {
        // Given a first name, when find by name starting with, return client
        String name = "Gian";
        given(clientRepository.findByNameStartingWith(name)).willReturn(answerClients);

        List<ClientDto> clientDto = clientService.findByNameStartingWith(name);

        assertThat(clientDto).isNotNull();
    }

    @Test
    void testFindClientById() {
        // Given an id, when find by id, return client
        Long id = 1l;
        given(clientRepository.findById(id)).willReturn(Optional.of(client));
        given(clientMapper.entityToDto(any())).willReturn(clientDto2);

        ClientDto clientDto = clientService.findClientById(id);

        assertThat(clientDto).isNotNull();
        assertThat(clientDto.id()).isEqualTo(1l);
    }

    @Test
    void testGetAllClients() {
        // When find all, return clients
        given(clientRepository.findAll()).willReturn(clients);

        List<ClientDto> clientsDto = clientService.getAllClients();

        assertThat(clientsDto).asList().hasSize(5);
    }

    @Test
    void testSaveClient() {
        // Given a client, when save, return client
        given(clientMapper.saveDtoToEntity(any())).willReturn(client);
        given(clientMapper.entityToDto(any())).willReturn(clientDto2);
        given(clientRepository.save(any())).willReturn(client);
        ClientToSaveDto clientToSave = new ClientToSaveDto("Gian Carlos Astori Goose", "duck_destroyer89@gmail.com", "Unimag Lake");
        ClientDto clientDto = clientService.saveClient(clientToSave);
        assertThat(clientDto).isNotNull();
        assertThat(clientDto.id()).isEqualTo(1l);
    }

    @Test
    void testUpdateClient() {
        // Given a client, when update, return client
        ClientDto clientDtoUpdate = new ClientDto(1l, "Gianso Marcos Astori", "duck_destroyer89@gmail.com", "Unimag Lake");
        Client clientUpdate = client;
        clientUpdate.setName("Gianso Marcos Astori");
        given(clientMapper.entityToDto(any())).willReturn(clientDtoUpdate);
        given(clientRepository.findById(any())).willReturn(Optional.of(client));
        given(clientRepository.save(any())).willReturn(clientUpdate);
        ClientToSaveDto clientToSave = new ClientToSaveDto("Gianso Marcos Astori", "duck_destroyer89@gmail.com", "Unimag Lake");
        ClientDto clientDto = clientService.updateClient( 1l,clientToSave);
        assertThat(clientDto).isNotNull();
        assertThat(clientDto.id()).isEqualTo(1l);
        assertThat(clientDto.name()).isEqualTo(clientToSave.name());
    }
}
