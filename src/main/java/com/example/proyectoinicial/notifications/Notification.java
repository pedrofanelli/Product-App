package com.example.proyectoinicial.notifications;

public interface Notification {
    void send(String message);
    String getType(); // Para identificarla (EMAIL, SMS, PUSH)
}
