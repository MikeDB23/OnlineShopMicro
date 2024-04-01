package com.shop.market.dto.product;

public record ProductDto(
                         Long id,
                         String name,
                         Double price,
                         Integer stock
) {}
