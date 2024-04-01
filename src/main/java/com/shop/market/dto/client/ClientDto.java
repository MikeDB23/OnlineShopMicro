package com.shop.market.dto.client;


public record ClientDto(
                        Long id, 
                        String name, 
                        String email, 
                        String address
) {}
