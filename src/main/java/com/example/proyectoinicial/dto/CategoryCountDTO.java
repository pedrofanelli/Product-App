package com.example.proyectoinicial.dto;

import lombok.Data;

@Data
public class CategoryCountDTO {
    private String categoryName;
    private Long productCount;

    // Importante: El constructor debe coincidir con el orden de la consulta JPQL
    public CategoryCountDTO(String categoryName, Long productCount) {
        this.categoryName = categoryName;
        this.productCount = productCount;
    }
}
