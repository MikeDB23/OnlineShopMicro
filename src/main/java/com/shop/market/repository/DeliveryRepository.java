package com.shop.market.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.market.entities.Delivery;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    //Pide buscar por ID
    List<Delivery> findByCompany(String company);
    //Hay uno que dice buscar envio por estado, no tengo ni idea de a que se refiere
}
