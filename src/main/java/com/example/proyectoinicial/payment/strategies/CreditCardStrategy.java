package com.example.proyectoinicial.payment.strategies;

import com.example.proyectoinicial.payment.PaymentStrategy;
import org.springframework.stereotype.Component;

@Component
public class CreditCardStrategy implements PaymentStrategy {
    @Override
    public void pay(double amount) {
        System.out.println("Pagando " + amount + " con Tarjeta de Crédito");
    }

    @Override
    public String getServiceType() {
        return "CREDIT_CARD";
    }
}
