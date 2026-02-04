package com.example.proyectoinicial.repositories;

import com.example.proyectoinicial.dto.CategoryCountDTO;
import com.example.proyectoinicial.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {

    Optional<List<Product>> findByNameContainingIgnoreCase(String name);

    //JPQL
    @Query("SELECT p FROM Product p JOIN FETCH p.category WHERE p.price > :minPrice AND p.category.name = :catName")
    List<Product> findPremiumProducts(@Param("minPrice") Double price, @Param("catName") String catName);

    //JPQL asignando a un DTO
    @Query("SELECT new com.example.proyectoinicial.dto.CategoryCountDTO(p.category.name, COUNT(p)) " +
            "FROM Product p " +
            "GROUP BY p.category.name")
    List<CategoryCountDTO> countProductsByCategory();

    //Native Query
    @Query(value = "SELECT c.name, COUNT(p.id) FROM Product p " +
            "JOIN Category c ON p.category_id = c.id " +
            "GROUP BY c.name", nativeQuery = true)
    List<Object[]> countProductsByCategoryNative();

}
