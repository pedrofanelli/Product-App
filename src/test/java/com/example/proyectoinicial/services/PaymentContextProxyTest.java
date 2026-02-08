package com.example.proyectoinicial.services;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import ch.qos.logback.classic.Logger;
import com.example.proyectoinicial.payment.context.PaymentContextImpl;
import com.example.proyectoinicial.payment.context.PaymentContextProxy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class PaymentContextProxyTest {

    private PaymentContextProxy proxy;
    private PaymentContextImpl realServiceMock;
    private ListAppender<ILoggingEvent> listAppender;

    @BeforeEach
    void setUp() {

        realServiceMock = mock(PaymentContextImpl.class);

        // El Proxy envuelve al Mock del servicio real
        proxy = new PaymentContextProxy(realServiceMock);

        // 1. Obtenemos el Logger de la clase (cast a la implementación de Logback)
        Logger paymentLogger = (Logger) LoggerFactory.getLogger(PaymentContextProxy.class);

        // 2. Creamos y empezamos un ListAppender (guarda los logs en una lista)
        listAppender = new ListAppender<>();
        listAppender.start();

        // 3. Lo añadimos al logger
        paymentLogger.addAppender(listAppender);

    }

    @Test
    void shouldSelectCreditCardStrategyWhenTypeIsCreditCard() {

        double amount = 100.0;

        // Ejecutar
        proxy.process("CREDIT_CARD", amount);

        // THEN: 1. ¿Delegó la llamada al servicio real?
        verify(realServiceMock, times(1)).process("CREDIT_CARD", amount);

        // THEN: 2. ¿Guardó el log de éxito?
        // 4. Verificar los mensajes capturados en la lista
        boolean hasStartMessage = listAppender.list.stream()
                .anyMatch(event -> event.getMessage().contains("Verificando límites de fraude")
                        && event.getLevel() == Level.INFO);

        boolean hasOKMessage = listAppender.list.stream()
                .anyMatch(event -> event.getMessage().contains("Pago realizado")
                        && event.getLevel() == Level.INFO);

        assertTrue(hasStartMessage);
        assertTrue(hasOKMessage);
    }

    @Test
    void shouldLogErrorWhenExceptionOccurs() {
        // Simular error
        doThrow(new RuntimeException("Falla técnica"))
                .when(realServiceMock).process(anyString(),anyDouble());

        // Ejecutar y capturar excepción
        try {
            proxy.process("CREDIT_CARD", 100.0);
        } catch (Exception ignored) {}

        verify(realServiceMock, times(1)).process("CREDIT_CARD", 100.0);

        // Verificar log de error
        boolean hasErrorMessage = listAppender.list.stream()
                .anyMatch(event -> event.getMessage().contains("[PROXY] Ocurrio un error")
                        && event.getLevel() == Level.ERROR);

        assertTrue(hasErrorMessage, "Debería haber un log de nivel ERROR");
    }

}
