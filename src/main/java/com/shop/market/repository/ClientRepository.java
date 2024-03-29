package com.shop.market.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.market.entities.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findByEmail(String email);
    List<Client> findByAddress(String address);
    List<Client> findByNameStartingWith(String name);
}
