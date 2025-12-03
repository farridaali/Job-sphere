package com.jobsphere.observer;

/**
 * DESIGN PATTERN: Observer Pattern
 * Why: Allows notification of multiple observers when job/application state changes
 * Affected Classes: Observer, NotificationObserver, ApplicationSubject
 */
public interface Observer {
    void update(String message);
}