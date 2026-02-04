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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository repository;

    @Autowired
    private TestEntityManager entityManager; // Alternativa limpia al repository.save

    private static final Logger log = LoggerFactory.getLogger(CategoryRepositoryTest.class);

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

    @Test
    void shouldCountProductsByCategoryUsingNativeQuery() {

        // GIVEN
        Category cat = new Category();
        cat.setName("Electronics");
        entityManager.persist(cat);

        Category cat2 = new Category();
        cat2.setName("Food");
        entityManager.persist(cat2);

        Category cat3 = new Category();
        cat3.setName("Furniture");
        entityManager.persist(cat3);

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

        List<Object[]> count = repository.countCategoriesAndCountProductsNative();

        List<CategoryCountDTO> list = new ArrayList<>();

        for  (Object[] row : count) {
            CategoryCountDTO dto = new CategoryCountDTO((String) row[0],(Long) row[1]);
            list.add(dto);
        }

        log.info(list.toString());

        assertFalse(list.isEmpty());
        assertEquals(3, list.size());
        assertEquals(0,list.getLast().getProductCount());

    }

}
