package com.shop.market.dto.client;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.shop.market.entities.Client;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    Client dtoToEntity(ClientDto clientDto);
    @Mapping(target = "id",ignore = true)
    Client saveDtoToEntity(ClientToSaveDto clientToSaveDto);
    ClientDto entityToDto(Client clientDto);
} 
