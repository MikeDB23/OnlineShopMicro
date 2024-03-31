package com.shop.market.dto.client;

import org.mapstruct.Mapper;

import com.shop.market.entities.Client;

@Mapper
public interface ClientMapper {
    Client dtoToEntity(ClientDto clientDto);
    Client saveDtoToEntity(ClientToSaveDto clientToSaveDto);
    ClientDto entityToDto(Client clientDto);
} 
