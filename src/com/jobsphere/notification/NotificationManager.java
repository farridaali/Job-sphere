package com.jobsphere.notification;

import java.util.*;

/**
 * OBSERVER PATTERN
 * Why: Notify users when application status changes or new jobs match criteria
 * Affected: Application status changes, new job postings
 * Benefit: Loose coupling between components, easy to add new notification types
 */
public class NotificationManager {
    private static NotificationManager instance;
    private List<NotificationObserver> observers;
    private List<Notification> notifications;

    public interface NotificationObserver {
        void update(Notification notification);
        String getObserverEmail();
    }

    public static class Notification {
        private String recipientEmail;
        private String message;
        private Date timestamp;
        private boolean read;

        public Notification(String recipientEmail, String message) {
            this.recipientEmail = recipientEmail;
            this.message = message;
            this.timestamp = new Date();
            this.read = false;
        }

        public String getRecipientEmail() { return recipientEmail; }
        public String getMessage() { return message; }
        public Date getTimestamp() { return timestamp; }
        public boolean isRead() { return read; }
        public void markAsRead() { this.read = true; }
    }

    private NotificationManager() {
        observers = new ArrayList<>();
        notifications = new ArrayList<>();
    }

    public static synchronized NotificationManager getInstance() {
        if (instance == null) {
            instance = new NotificationManager();
        }
        return instance;
    }

    public void addObserver(NotificationObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(NotificationObserver observer) {
        observers.remove(observer);
    }

    public void notifyApplicationStatusChange(String applicantEmail, String jobTitle, String newStatus) {
        String message = "Your application for " + jobTitle + " status changed to: " + newStatus;
        Notification notification = new Notification(applicantEmail, message);
        notifications.add(notification);
        notifyObservers(notification);
    }

    public void notifyNewApplication(String companyEmail, String applicantName, String jobTitle) {
        String message = applicantName + " applied for your job: " + jobTitle;
        Notification notification = new Notification(companyEmail, message);
        notifications.add(notification);
        notifyObservers(notification);
    }

    private void notifyObservers(Notification notification) {
        for (NotificationObserver observer : observers) {
            if (observer.getObserverEmail().equals(notification.getRecipientEmail())) {
                observer.update(notification);
            }
        }
    }

    public List<Notification> getNotificationsForUser(String email) {
        List<Notification> userNotifications = new ArrayList<>();
        for (Notification notification : notifications) {
            if (notification.getRecipientEmail().equals(email)) {
                userNotifications.add(notification);
            }
        }
        return userNotifications;
    }
}