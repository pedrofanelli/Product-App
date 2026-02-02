package com.example.proyectoinicial.entities;

import jakarta.persistence.*;
import lombok.Data;

/* TABLA DE SQL */

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double price;
    private Integer stock;
    private Boolean outOfStock = false; // Valor por defecto

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
