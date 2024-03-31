package com.shop.market.dto.product;

public record ProductToSaveDto(
                                String name,
                                Double price,
                                Integer stock
){}