package com.shop.market.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shop.market.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
    List<Product> findByNameLike(String searchTerm);
    @Query("SELECT p FROM Product p WHERE p.stock > 0")
    List<Product> findByStockGreaterThanZero();
    List<Product> findByPriceLessThanAndStockLessThan(Double price, Integer stock);
}
