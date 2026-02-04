package com.example.proyectoinicial.notifications.factory;

import com.example.proyectoinicial.notifications.Notification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Acá usamos la FACTORY PATTERN
 * Híbrido porque también usamos STRATEGY
 * Mientras que el Strategy se encarga de cómo hacer algo (pagar),
 * el Factory se encarga de quién o qué objeto crear.
 */

@Component
public class NotificationFactory {
    private final Map<String, Notification> notificationMap;

    // Al igual que con el Strategy, Spring nos llena el mapa automáticamente
    public NotificationFactory(List<Notification> notifications) {
        this.notificationMap = notifications.stream()
                .collect(Collectors.toMap(Notification::getType, n -> n));
    }

    public Notification getNotification(String type) {
        Notification notification = notificationMap.get(type.toUpperCase());
        if (notification == null) {
            throw new IllegalArgumentException("Tipo de notificación no soportado: " + type);
        }
        return notification;
    }
}
