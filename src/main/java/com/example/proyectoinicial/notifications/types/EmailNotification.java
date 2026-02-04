package com.example.proyectoinicial.notifications.types;

import com.example.proyectoinicial.notifications.Notification;
import org.springframework.stereotype.Component;

@Component
public class EmailNotification implements Notification {
    public void send(String msg) { System.out.println("Enviando Email: " + msg); }
    public String getType() { return "EMAIL"; }
}
