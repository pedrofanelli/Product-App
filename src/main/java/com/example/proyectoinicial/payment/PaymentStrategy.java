package com.example.proyectoinicial.payment;

public interface PaymentStrategy {
    void pay(double amount);
    String getServiceType(); // En lugar de supports(type)
}
