package com.example.proyectoinicial.services;

import com.example.proyectoinicial.notifications.Notification;
import com.example.proyectoinicial.notifications.factory.NotificationFactory;
import org.springframework.stereotype.Service;

@Service
public class UserAlertService {
    private final NotificationFactory factory;

    public UserAlertService(NotificationFactory factory) {
        this.factory = factory;
    }

    public void notifyUser(String userPreference, String message) {
        // Obtenemos la preferencia del usuario (ej: "SMS")

        // La Factory decide qué objeto darnos
        Notification notification = factory.getNotification(userPreference);

        // Usamos el objeto (Polimorfismo)
        notification.send(message);
    }
}
