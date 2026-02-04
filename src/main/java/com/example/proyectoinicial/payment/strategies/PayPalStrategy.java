package com.example.proyectoinicial.payment.strategies;

import com.example.proyectoinicial.payment.PaymentStrategy;
import org.springframework.stereotype.Component;

@Component
public class PayPalStrategy implements PaymentStrategy {
    @Override
    public void pay(double amount) {
        System.out.println("Pagando " + amount + " con PayPal");
    }
    @Override
    public String getServiceType() {
        return "PAYPAL";
    }
}
