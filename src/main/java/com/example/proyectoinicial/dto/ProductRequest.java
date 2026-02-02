package com.example.proyectoinicial.dto;

/*
* Usaremos la dependencia Validation para asegurar que los datos sean correctos
* antes de llegar al servicio.
* */

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductRequest {
    @NotBlank(message = "El nombre no puede estar vacío")
    private String name;

    @Min(value = 0, message = "El precio debe ser mayor o igual a 0")
    @NotNull(message = "El precio es obligatorio")
    private Double price;

    @Min(value = 0, message = "El stock inicial no puede ser negativo")
    private Integer stock; // Nuevo campo
}
