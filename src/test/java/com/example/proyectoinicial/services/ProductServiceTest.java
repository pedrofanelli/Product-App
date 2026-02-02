package com.example.proyectoinicial.services;

import com.example.proyectoinicial.dto.ProductRequest;
import com.example.proyectoinicial.entities.Product;
import com.example.proyectoinicial.exceptions.InsufficientStockException;
import com.example.proyectoinicial.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Usamos Mockito para simular dependencias
public class ProductServiceTest {
    @Mock
    private ProductRepository repository; // Simulamos el repositorio

    @InjectMocks
    private ProductService productService; // Inyectamos el mock en el servicio

    /*
    * "Uso Mocks para aislar la lógica de negocio.
    * Un Test Unitario debe probar solo la clase en cuestión.
    * Si usara una base de datos real en el test del servicio, estaría haciendo un test de integración,
    * lo cual es más lento y hace que sea más difícil identificar si el error está en la lógica del Java
    * o en la configuración de la persistencia."
    * */

    @Test
    void shouldSaveProductSuccessfully() {
        // 1. Arrange (Preparar los datos)
        ProductRequest request = new ProductRequest();
        request.setName("Laptop");
        request.setPrice(1000.0);

        Product savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setName("Laptop");
        savedProduct.setPrice(1000.0);

        // Simulamos que cuando el repo guarde cualquier cosa, devuelva nuestro producto con ID
        when(repository.save(any(Product.class))).thenReturn(savedProduct);

        // 2. Act (Ejecutar la acción)
        // esto es lo que probamos, que el código del Service funcione
        // y luego que se ejecute 1 vez el repositorio
        Product result = productService.save(request);

        // 3. Assert (Verificar que sea correcto)
        assertNotNull(result.getId());
        assertEquals("Laptop", result.getName());
        verify(repository, times(1)).save(any(Product.class)); // Verificamos que se llamó al repo
    }

    @Test
    void shouldThrowExceptionWhenStockIsInsufficient() {
        // Arrange: Producto con stock 5
        Product productInDb = new Product();
        productInDb.setId(1L);
        productInDb.setStock(5);

        when(repository.findById(1L)).thenReturn(Optional.of(productInDb));

        // Act & Assert: Intentamos restar 10
        assertThrows(InsufficientStockException.class, () -> {
            productService.subtractStock(1L, 10);
        });
    }

    @Test
    void shouldDecreaseStockSuccessfully() {

        // 1. Arrange
        Product productInDb = new Product();
        productInDb.setId(1L);
        productInDb.setStock(10);
        productInDb.setOutOfStock(false);

        when(repository.findById(1L)).thenReturn(Optional.of(productInDb));

        // 2. Act
        productService.subtractStock(1L, 10);

        // 3. Assert (Aquí viene la magia)
        // Verificamos que se llamó al save y capturamos qué se intentó guardar
        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(repository).save(captor.capture());

        Product savedProduct = captor.getValue(); // Este es el producto después de la lógica del service

        assertEquals(0, savedProduct.getStock(), "El stock debería ser 0");
        assertTrue(savedProduct.getOutOfStock(), "El producto debería estar marcado como agotado");

    }

    @Test
    void shouldReturnProductByName() {
        Product productInDbOne = new Product();
        productInDbOne.setId(1L);
        productInDbOne.setName("Laptop");
        productInDbOne.setPrice(1000.0);
        productInDbOne.setOutOfStock(false);
        productInDbOne.setStock(10);

        Product productInDbTwo = new Product();
        productInDbTwo.setId(2L);
        productInDbTwo.setName("Gaming PC");
        productInDbTwo.setPrice(5750.0);
        productInDbTwo.setOutOfStock(false);
        productInDbTwo.setStock(8);

        Product productInDbThree = new Product();
        productInDbThree.setId(3L);
        productInDbThree.setName("Laptop Otro Tipo");
        productInDbThree.setPrice(2200.0);
        productInDbThree.setOutOfStock(false);
        productInDbThree.setStock(4);

        List<Product> products = new ArrayList<>();
        products.add(productInDbOne);
        products.add(productInDbThree);

        when(repository.findByNameContainingIgnoreCase("LAPTOP"))
                .thenReturn(Optional.of(products));

        List<Product> productList = productService.findAllByName("LAPTOP");

        assertEquals(productInDbOne, productList.get(0));
        assertEquals(productInDbThree, productList.get(1));

    }

}
