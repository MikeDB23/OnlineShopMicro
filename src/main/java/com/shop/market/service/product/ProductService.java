package com.shop.market.service.product;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shop.market.dto.product.ProductDto;
import com.shop.market.dto.product.ProductToSaveDto;

@Service
public interface ProductService {
    ProductDto saveProduct(ProductToSaveDto productToSaveDto);
    ProductDto updateProduct(Long id, ProductToSaveDto productToSaveDto);
    ProductDto findProductById(Long id);
    void deleteProduct(Long id);
    List<ProductDto> getAllProducts(); 

    List<ProductDto> findByNameLike(String searchTerm);
    List<ProductDto> findByStockGreaterThanZero();
    List<ProductDto> findByPriceLessThanAndStockLessThan(Double price, Integer stock);
}
