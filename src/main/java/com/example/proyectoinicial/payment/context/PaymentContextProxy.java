package com.example.proyectoinicial.payment.context;

import com.example.proyectoinicial.payment.PaymentStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class PaymentContextProxy implements IPaymentContext {

    private final PaymentContextImpl realService;

    // Definimos el logger
    private static final Logger log = LoggerFactory.getLogger(PaymentContextProxy.class);

    public PaymentContextProxy(PaymentContextImpl realService) {
        this.realService = realService;
    }

    @Override
    public void process(String type, double amount) {
        // ACCIÓN ANTES: Auditoría de seguridad
        log.info("[PROXY] Verificando límites de fraude para monto: " + amount);

        try {
            // DELEGACIÓN: Llamamos al servicio real (que usa la Factory y la Strategy)
            realService.process(type, amount);

            // ACCIÓN DESPUÉS (Éxito):
            log.info("[PROXY] Pago realizado.");
        } catch (Exception e) {
            // ACCIÓN DESPUÉS (Error):
            log.error("[PROXY] Ocurrio un error: " + e.getMessage());
            throw e;
        }
    }

}
