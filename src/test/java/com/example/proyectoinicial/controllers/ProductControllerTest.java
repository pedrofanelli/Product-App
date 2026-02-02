package com.example.proyectoinicial.controllers;

/*
* Usamos MockMvc para simular una llamada HTTP real (como si viniera de Postman).
* */

/*
* Con esto habrás probado las 3 capas: Repositorio (DB),
* Servicio (Lógica) y Controlador (API).
* */

import com.example.proyectoinicial.entities.Product;
import com.example.proyectoinicial.services.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
// Para status(), content(), get(), post(), etc.
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Para MediaType (esta no suele ser estática, es la clase)
import org.springframework.http.MediaType;

@WebMvcTest(ProductController.class) // Solo levanta la capa Web
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc; // Simula el cliente HTTP

    @MockitoBean
    private ProductService productService; // Simulamos el servicio

    @Autowired
    private ObjectMapper objectMapper; // Para convertir objetos a JSON

    @Test
    void shouldReturnAllProducts() throws Exception {

        // Creamos el producto
        Product product = new Product();
        product.setName("Laptop");
        product.setPrice(25.0);


        // Arrange
        when(productService.findAll()).thenReturn(List.of(product));

        // Act & Assert
        /*mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk()) // Verifica que devuelve 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));*/

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Laptop")) // El primer elemento tiene nombre "Laptop"
                .andExpect(jsonPath("$.length()").value(1));      // La lista tiene un tamaño de 1


    }

    @Test
    void shouldPatchProduct() throws Exception {

        // 1. Arrange
        Long productId = 1L;
        Integer quantityToSell = 5;

        // No necesitamos 'when' porque el método es void y Mockito ya sabe no hacer nada.
        // Pero si quieres asegurar que no lance error:
        doNothing().when(productService).subtractStock(eq(productId), eq(quantityToSell));

        // 2. Act & Assert
        mockMvc.perform(patch("/api/products/{id}/sell", productId)
                        .param("quantity", quantityToSell.toString()) // Enviamos el @RequestParam
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNoContent()); // Si usaste .noContent() en el controller, es 204

        // 3. Verificación extra (Opcional pero muy recomendada)
        verify(productService, times(1)).subtractStock(productId, quantityToSell);

    }

    @Test
    void shouldReturnProductByName() throws Exception {

        Product product = new Product();
        product.setName("Laptop");
        product.setPrice(25.0);

        Product product2 = new Product();
        product2.setName("Laptop GRANDE");
        product2.setPrice(50.0);

        Product product3 = new Product();
        product3.setName("LAPTOP ONE");
        product3.setPrice(60.0);

        // Arrange
        when(productService.findAllByName("Laptop")).thenReturn(List.of(product, product2, product3));

        mockMvc.perform(get("/api/products")
                        .param("name", "Laptop") // Enviamos el @RequestParam
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.length()").value(3))
                        .andExpect(jsonPath("$[0].name").value("Laptop"))
                        .andExpect(jsonPath("$[1].name").value("Laptop GRANDE"))
                        .andExpect(jsonPath("$[2].price").value(60.0));

        // Después del mockMvc.perform
        verify(productService, times(1)).findAllByName("Laptop");
    }

}
