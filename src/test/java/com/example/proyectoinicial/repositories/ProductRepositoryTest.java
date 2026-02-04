package com.example.proyectoinicial.repositories;

import com.example.proyectoinicial.dto.CategoryCountDTO;
import com.example.proyectoinicial.entities.Category;
import com.example.proyectoinicial.entities.Product;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest // Levanta JPA y una DB real en memoria
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository; // Aquí NO es un Mock, es el real

    @Autowired
    private TestEntityManager entityManager; // Alternativa limpia al repository.save

    // Definimos el logger
    private static final Logger log = LoggerFactory.getLogger(ProductRepositoryTest.class);

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

    @Test
    void shouldReturnEmptyListWhenNoProductsPremiumMatch() {

        // GIVEN
        Category cat = new Category();
        cat.setName("Electronics");
        entityManager.persist(cat);

        Product p = new Product();
        p.setName("Laptop");
        p.setPrice(1500.0);
        p.setCategory(cat);
        entityManager.persist(p);
        entityManager.flush();

        // WHEN
        List<Product> results = repository.findPremiumProducts(2000.0, "Electronics");

        // THEN
        assertTrue(results.isEmpty()); // Aquí validamos lo que hablamos antes


    }

    @Test
    void shouldReturnProductsPremiumMatch() {

        // GIVEN
        Category cat = new Category();
        cat.setName("Electronics");
        entityManager.persist(cat);

        Category cat2 = new Category();
        cat2.setName("Food");
        entityManager.persist(cat2);

        Product p = new Product();
        p.setName("Laptop");
        p.setPrice(1500.0);
        p.setCategory(cat);
        entityManager.persist(p);

        p = new Product();
        p.setName("PC");
        p.setPrice(2000.0);
        p.setCategory(cat);
        entityManager.persist(p);

        p = new Product();
        p.setName("Apple");
        p.setPrice(2.0);
        p.setCategory(cat2);
        entityManager.persist(p);

        p = new Product();
        p.setName("Banana");
        p.setPrice(3.0);
        p.setCategory(cat2);
        entityManager.persist(p);

        entityManager.flush();

        // WHEN
        List<Product> results = repository.findPremiumProducts(1700.0, "Electronics");

        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals("PC",results.getFirst().getName());
        assertEquals("Electronics",results.getFirst().getCategory().getName());
    }

    @Test
    void shouldCountProductsByCategory() {
        // GIVEN
        Category cat = new Category();
        cat.setName("Electronics");
        entityManager.persist(cat);

        Category cat2 = new Category();
        cat2.setName("Food");
        entityManager.persist(cat2);

        Product p = new Product();
        p.setName("Laptop");
        p.setPrice(1500.0);
        p.setCategory(cat);
        entityManager.persist(p);

        p = new Product();
        p.setName("PC");
        p.setPrice(2000.0);
        p.setCategory(cat);
        entityManager.persist(p);

        p = new Product();
        p.setName("Apple");
        p.setPrice(2.0);
        p.setCategory(cat2);
        entityManager.persist(p);

        p = new Product();
        p.setName("Banana");
        p.setPrice(3.0);
        p.setCategory(cat2);
        entityManager.persist(p);

        p = new Product();
        p.setName("Monitor");
        p.setPrice(1105.0);
        p.setCategory(cat);
        entityManager.persist(p);

        entityManager.flush();

        List<CategoryCountDTO> count = repository.countProductsByCategory();

        log.info(count.toString());

        assertFalse(count.isEmpty());
        assertEquals(2, count.size());

    }

    @Test
    void shouldCountProductsByCategoryUsingNativeQuery() {

        // GIVEN
        Category cat = new Category();
        cat.setName("Electronics");
        entityManager.persist(cat);

        Category cat2 = new Category();
        cat2.setName("Food");
        entityManager.persist(cat2);

        Product p = new Product();
        p.setName("Laptop");
        p.setPrice(1500.0);
        p.setCategory(cat);
        entityManager.persist(p);

        p = new Product();
        p.setName("PC");
        p.setPrice(2000.0);
        p.setCategory(cat);
        entityManager.persist(p);

        p = new Product();
        p.setName("Apple");
        p.setPrice(2.0);
        p.setCategory(cat2);
        entityManager.persist(p);

        p = new Product();
        p.setName("Banana");
        p.setPrice(3.0);
        p.setCategory(cat2);
        entityManager.persist(p);

        p = new Product();
        p.setName("Monitor");
        p.setPrice(1105.0);
        p.setCategory(cat);
        entityManager.persist(p);

        entityManager.flush();

        List<Object[]> count = repository.countProductsByCategoryNative();

        List<CategoryCountDTO> list = new ArrayList<>();

        for  (Object[] row : count) {
            CategoryCountDTO dto = new CategoryCountDTO((String) row[0],(Long) row[1]);
            list.add(dto);
        }

        log.info(list.toString());

        assertFalse(list.isEmpty());
        assertEquals(2, list.size());

    }
}
