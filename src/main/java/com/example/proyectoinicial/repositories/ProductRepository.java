package com.example.proyectoinicial.repositories;

import com.example.proyectoinicial.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {

    Optional<List<Product>> findByNameContainingIgnoreCase(String name);

}
