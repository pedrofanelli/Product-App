package com.example.proyectoinicial.repositories;

import com.example.proyectoinicial.dto.CategoryCountDTO;
import com.example.proyectoinicial.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {

    //Native Query
    @Query(value = "SELECT c.name, COUNT(p.id) FROM Category c " +
            "LEFT JOIN Product p ON c.id = p.category_id " +
            "GROUP BY c.name", nativeQuery = true)
    List<Object[]> countCategoriesAndCountProductsNative();


}
