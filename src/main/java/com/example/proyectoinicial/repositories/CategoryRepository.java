package com.example.proyectoinicial.repositories;

import com.example.proyectoinicial.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
