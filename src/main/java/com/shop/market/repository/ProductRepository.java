package com.shop.market.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.market.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
    List<Product> findByNameLike(String searchTerm);
    List<Product> findByStockGreaterThan(Integer zero); //Como hacemos un Query que tenga un parametro por defecto?
    List<Product> findByPriceLessThanAndStockLessThan(Double price, Integer stock);
}
