package com.example.proyectoinicial.services;

import com.example.proyectoinicial.payment.PaymentStrategy;
import com.example.proyectoinicial.payment.context.PaymentContextImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class PaymentContextImplTest {

    private PaymentContextImpl paymentContext;
    private PaymentStrategy creditCardMock;
    private PaymentStrategy paypalMock;

    @BeforeEach
    void setUp() {
        // 1. Creamos mocks de las estrategias
        creditCardMock = mock(PaymentStrategy.class);
        paypalMock = mock(PaymentStrategy.class);

        // 2. Configuramos el comportamiento de "getServiceType" (o supports)
        when(creditCardMock.getServiceType()).thenReturn("CREDIT_CARD");
        when(paypalMock.getServiceType()).thenReturn("PAYPAL");

        // 3. Instanciamos el contexto manualmente pasando la lista de mocks
        // Esto simula lo que Spring haría automáticamente
        paymentContext = new PaymentContextImpl(List.of(creditCardMock, paypalMock));
    }

    @Test
    void shouldSelectCreditCardStrategyWhenTypeIsCreditCard() {
        double amount = 100.0;

        // Ejecutar
        paymentContext.process("CREDIT_CARD", amount);

        // Verificar: Se debió llamar a la estrategia de tarjeta y NO a la de PayPal
        verify(creditCardMock, times(1)).pay(amount);
        verify(paypalMock, never()).pay(anyDouble());
    }

    @Test
    void shouldThrowExceptionWhenTypeIsInvalid() {
        // Verificar que lanza la excepción adecuada ante un tipo inexistente
        assertThrows(IllegalArgumentException.class, () -> {
            paymentContext.process("BITCOIN", 50.0);
        });
    }

}
