package com.shop.market.dto.product;

import org.mapstruct.Mapper;

import com.shop.market.entities.Product;

@Mapper
public interface ProductMapper {
    Product dtoToEntity(ProductDto productDto);
    Product saveDtoToEntity(ProductToSaveDto productToSaveDto);
    ProductDto entityToDto(Product product);
}
