package com.example.proyectoinicial.exceptions;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/*
* Nuestro DTO que verá el usuario
* */

@Data
@Builder
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private Map<String, String> validations; // Para errores de @Valid
}
