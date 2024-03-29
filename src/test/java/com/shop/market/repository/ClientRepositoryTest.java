package com.shop.market.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import com.shop.market.AbstractsIntegrationDBTest;
import com.shop.market.TestUtil;
import com.shop.market.entities.Client;

public class ClientRepositoryTest extends AbstractsIntegrationDBTest{
    ClientRepository clientRepository;
    
    @Autowired
    public ClientRepositoryTest(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    void initClientTest(){
        List<Client> clients = TestUtil.genClients();
        clientRepository.saveAll(clients);
        clientRepository.flush();
    }
    
    @BeforeEach
    void setUp() {
        clientRepository.deleteAll();
    }
    
    //create test
    @Test
    void givenAClient_whenSave_thenClientWithId(){
        Client client = Client.builder()
                            .name("Miguel Acetato")
                            .email("miguelitoA@gmail.com")
                            .address("Manzana 40 casa 10 El Pando")
                            .build();
        Client clientSaved = clientRepository.save(client);
        assertThat(clientSaved.getId()).isNotNull();
        assertThat(clientRepository.findAll()).hasSize(1);
    }

    //read test
    @Test
    void whenFindAll_thenGetAllClients(){
        initClientTest();
        List<Client> clients = clientRepository.findAll();
        assertThat(clients).isNotEmpty();
        assertThat(clients).hasSize(5);
    }

    //Update test
    @Test
    void givenAClientAndEmail_whenSetEmail_thenUpdateUsersEmail(){
        initClientTest();
        List<Client> clients = clientRepository.findAll(); 
        Client client = clients.get(0);
        Client original_client = clients.get(0);

        client.setEmail("peacefulgoose@gmail.com");
        clientRepository.save(client);

        Optional<Client> updatedClientOptional = clientRepository.findById(original_client.getId());
        assertTrue(updatedClientOptional.isPresent(), "Client not found");

        Client updatedClient = updatedClientOptional.get();
        assertEquals(client.getName(), updatedClient.getName(), "Email not updated");
    }
    
    //Delete Test
    @Test
    void givenAClient_whenDelete_thenDeleteClient(){
        initClientTest();
        Client client = clientRepository.findAll().get(4);
        Long size  = clientRepository.count();

        clientRepository.delete(client);
        assertEquals(size-1, clientRepository.count());
    }

    @Test
    void givenAnEmail_whenFindByEmail_thenGetClientsWithEmail(){
        initClientTest();
        Client client = clientRepository.findAll().get(3);
        List<Client> clients = clientRepository.findByEmail(client.getEmail());
        
        assertThat(clients).isNotEmpty();
        assertThat(clients).hasSize(1);
    }

    @Test
    void givenAnAddress_whenFindByAddress_thenGetClientsWithAddress(){
        initClientTest();
        Client client = clientRepository.findAll().get(3);
        List<Client> clients = clientRepository.findByAddress(client.getAddress());
        
        assertThat(clients).isNotEmpty();
        assertThat(clients).hasSize(1);
    }
    
    @Test
    void givenAName_whenFindByNameStartingWith_thenGetClientsWhoseNameStartWithNameProvided(){
        initClientTest();
        List<Client> clients = clientRepository.findByNameStartingWith("Juan");
        assertThat(clients).hasSize(3);
    }
}
