package com.example.proyectoinicial.repositories;

import com.example.proyectoinicial.entities.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest // Levanta JPA y una DB real en memoria
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository; // Aquí NO es un Mock, es el real

    @Test
    void shouldPersistProductInDatabase() {
        // Arrange
        Product product = new Product();
        product.setName("Mouse");
        product.setPrice(25.0);

        // Act
        Product saved = repository.save(product);

        // Assert
        assertNotNull(saved.getId()); // Aquí verificamos que la DB le asignó un ID real
        assertEquals("Mouse", repository.findById(saved.getId()).get().getName());
    }

    @Test
    void shouldFindProductsByName() {
        Product product = new Product();
        product.setName("Mouse");
        product.setPrice(25.0);

        Product saved = repository.save(product);

        product = new Product();
        product.setName("Keyboard");
        product.setPrice(35.0);

        Product saved2 = repository.save(product);

        product = new Product();
        product.setName("Big MOUSE");
        product.setPrice(55.0);
        Product saved3 = repository.save(product);

        assertNotNull(saved.getId());
        assertNotNull(saved2.getId());
        assertNotNull(saved3.getId());

        assertEquals(2,repository.findByNameContainingIgnoreCase("MOUSE")
                .orElse(Collections.emptyList()).size());
        assertEquals("Mouse",repository.findByNameContainingIgnoreCase("MOUSE")
                .orElseThrow().getFirst().getName());
        assertEquals("Big MOUSE",repository.findByNameContainingIgnoreCase("MOUSE")
                .orElseThrow().getLast().getName());
        assertNotEquals("Keyboard",repository.findByNameContainingIgnoreCase("MOUSE")
                .orElseThrow().getFirst().getName());
        assertNotEquals("Keyboard",repository.findByNameContainingIgnoreCase("MOUSE")
                .orElseThrow().getLast().getName());

    }
}
