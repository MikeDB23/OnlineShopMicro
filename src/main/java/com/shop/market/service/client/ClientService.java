package com.shop.market.service.client;

import java.util.List;

import com.shop.market.dto.client.ClientDto;
import com.shop.market.dto.client.ClientToSaveDto;

public interface ClientService {
    ClientDto saveClient(ClientToSaveDto clientToSaveDto);
    ClientDto updateClient(Long id, ClientToSaveDto clientToSaveDto);
    ClientDto findClientById(Long id);
    void deleteClient(Long id);
    List<ClientDto> getAllClients(); 
    
    List<ClientDto> findByEmail(String email);
    List<ClientDto> findByAddress(String address);
    List<ClientDto> findByNameStartingWith(String name);
}
