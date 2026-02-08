package com.example.proyectoinicial.payment.context;

import com.example.proyectoinicial.payment.PaymentStrategy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * PATRON DE DISEÑO "ESTRATEGIA"
 * Aplica la O en SOLID (open/closed principle):
 * Open to extension (new payments), closed to modification (we don't touch the context)
 */

@Service
public class PaymentContextImpl implements IPaymentContext {

    private final Map<String, PaymentStrategy> strategies;

    // Spring inyecta todas las estrategias a la lista y nosotros las mapeamos al iniciar
    public PaymentContextImpl(List<PaymentStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(
                        PaymentStrategy::getServiceType, // La clave es el tipo (e.g., "PAYPAL")
                        s -> s    // El valor es la instancia
                ));
    }

    @Override
    public void process(String type, double amount) {
        PaymentStrategy strategy = strategies.get(type.toUpperCase());

        if (strategy == null) {
            throw new IllegalArgumentException("Método de pago no válido: " + type);
        }

        strategy.pay(amount);
    }

}
