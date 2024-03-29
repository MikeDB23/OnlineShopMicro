package com.shop.market.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shop.market.entities.OrderItem;
import com.shop.market.entities.Product;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrderId(Long order);
    List<OrderItem> findByProduct(Product product);
    @Query("SELECT SUM(i.amount * i.product.price) FROM OrderItem i WHERE i.product = :p")
    Double getProductSales(@Param("p") Product product);
}
