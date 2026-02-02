package com.example.proyectoinicial.services;

import com.example.proyectoinicial.dto.ProductRequest;
import com.example.proyectoinicial.entities.Category;
import com.example.proyectoinicial.entities.Product;
import com.example.proyectoinicial.exceptions.CategoryNotFoundException;
import com.example.proyectoinicial.exceptions.InsufficientStockException;
import com.example.proyectoinicial.repositories.CategoryRepository;
import com.example.proyectoinicial.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/* @RequiredArgsConstructor
* Notación de Lombok genera un constructor en tiempo de compilación que incluye todos
* los campos marcados como final o aquellos marcados con @NonNull.
* */

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;

    public List<Product> findAll(){
        return repository.findAll();
    }

    @Transactional
    public Product save(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        product.setCategory(category);

        return repository.save(product);
    }

    @Transactional
    public void subtractStock(Long id, Integer quantity) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (product.getStock() < quantity) {
            throw new InsufficientStockException("No hay stock suficiente");
        }

        product.setStock(product.getStock() - quantity);

        // Lógica del booleano "agotado"
        if (product.getStock() == 0) {
            product.setOutOfStock(true);
        }

        repository.save(product);
    }

    public List<Product> findAllByName(String name){

        return repository.findByNameContainingIgnoreCase(name)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }
}
