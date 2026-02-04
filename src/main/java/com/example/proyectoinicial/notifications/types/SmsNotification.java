package com.example.proyectoinicial.notifications.types;

import com.example.proyectoinicial.notifications.Notification;
import org.springframework.stereotype.Component;

@Component
public class SmsNotification implements Notification {
    public void send(String msg) { System.out.println("Enviando SMS: " + msg); }
    public String getType() { return "SMS"; }
}
