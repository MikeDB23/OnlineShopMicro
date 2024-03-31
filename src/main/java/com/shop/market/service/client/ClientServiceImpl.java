package com.shop.market.service.client;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shop.market.dto.client.ClientDto;
import com.shop.market.dto.client.ClientMapper;
import com.shop.market.dto.client.ClientToSaveDto;
import com.shop.market.entities.Client;
import com.shop.market.exceptions.NotAbleToDeleteException;
import com.shop.market.exceptions.NotFoundException;
import com.shop.market.repository.ClientRepository;

@Service
public class ClientServiceImpl implements ClientService{
    private final ClientMapper clientMapper;
    private final ClientRepository clientRepository;

    
    public ClientServiceImpl(ClientMapper clientMapper, ClientRepository clientRepository) {
        this.clientMapper = clientMapper;
        this.clientRepository = clientRepository;
    }

    @Override
    public ClientDto saveClient(ClientToSaveDto clientToSaveDto) {
        Client client = clientMapper.saveDtoToEntity(clientToSaveDto);
        Client savedClient = clientRepository.save(client);
        return clientMapper.entityToDto(savedClient);
    }
    
    @Override
    public ClientDto findClientById(Long id) {
        Client client = clientRepository.findById(id)
                            .orElseThrow(()-> new NotFoundException("Client not found"));
        return clientMapper.entityToDto(client);
    }

    @Override
    public ClientDto updateClient(Long id, ClientToSaveDto client) {
        return clientRepository.findById(id).map(clientDB -> {
            clientDB.setName(client.name());
            clientDB.setEmail(client.email());
            clientDB.setAddress(client.address());

            Client savedClient = clientRepository.save(clientDB);
            return clientMapper.entityToDto(savedClient);
        }).orElseThrow(()-> new NotFoundException("Client not found"));
    }

    @Override
    public void deleteClient(Long id) {
        Client client = clientRepository.findById(id)
                            .orElseThrow(()-> new NotAbleToDeleteException("Client not found"));
        clientRepository.delete(client);
        
    }

    @Override
    public List<ClientDto> getAllClients() {
        List<Client> clients = clientRepository.findAll();
        return clients.stream()
                .map(client -> clientMapper.entityToDto(client))
                .toList();
    }

    @Override
    public List<ClientDto> findByAddress(String address) {
        List<Client> clients = clientRepository.findByAddress(address);
        return clients.stream()
                .map(client -> clientMapper.entityToDto(client))
                .toList();
    }

    @Override
    public List<ClientDto> findByEmail(String email) {
        List<Client> clients = clientRepository.findByEmail(email);
        return clients.stream()
                .map(client -> clientMapper.entityToDto(client))
                .toList();
    }

    @Override
    public List<ClientDto> findByNameStartingWith(String name) {
        List<Client> clients = clientRepository.findByNameStartingWith(name);
        return clients.stream()
                .map(client -> clientMapper.entityToDto(client))
                .toList();
    }
    
}
