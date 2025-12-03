// NotificationObserver.java
package com.jobsphere.observer;

import javax.swing.JOptionPane;

public class NotificationObserver implements Observer {
    private String observerName;

    public NotificationObserver(String name) {
        this.observerName = name;
    }

    @Override
    public void update(String message) {
        System.out.println("Notification for " + observerName + ": " + message);
        // In a real application, this could send emails or push notifications
    }
}