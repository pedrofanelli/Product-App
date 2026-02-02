package com.example.proyectoinicial.repositories;

import com.example.proyectoinicial.entities.Category;
import com.example.proyectoinicial.entities.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository repository;

    @Test
    void shouldPersistCategoryInDatabase() {
        // Arrange
        Category category = new Category();
        category.setName("Category 1");

        // Act
        Category saved = repository.save(category);

        // Assert
        assertNotNull(saved.getId()); // Aquí verificamos que la DB le asignó un ID real
        assertEquals("Category 1", repository.findById(saved.getId()).get().getName());
    }

}
