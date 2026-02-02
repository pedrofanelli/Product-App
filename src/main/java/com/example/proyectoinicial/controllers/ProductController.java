package com.example.proyectoinicial.controllers;

import com.example.proyectoinicial.dto.ProductRequest;
import com.example.proyectoinicial.entities.Product;
import com.example.proyectoinicial.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService service;
    public ProductController(ProductService service){
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Product>> findAll(@RequestParam Optional<String> name){

        if (name.isPresent()){
            return new ResponseEntity<>(service.findAllByName(name.get()), HttpStatus.OK);
        }

        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }


// @Valid esta conectado con la libreria Validation, y el codigo en el dto ProductRequest

    @PostMapping
    public ResponseEntity<Product> create(@Valid @RequestBody ProductRequest request) {
        Product savedProduct = service.save(request);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/sell")
    public ResponseEntity<Void> sellProduct(@PathVariable Long id, @RequestParam Integer quantity) {
        service.subtractStock(id, quantity);
        return ResponseEntity.noContent().build(); // 204 No Content es ideal para actualizaciones exitosas sin cuerpo
    }
}
