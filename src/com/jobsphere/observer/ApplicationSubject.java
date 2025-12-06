package com.jobsphere.observer;

public class ApplicationSubject extends Subject {

    public void applicationSubmitted(String applicantName, String jobTitle) {
        notifyObservers("New application from " + applicantName + " for " + jobTitle);
    }

    public void applicationStatusChanged(String applicantName, String status) {
        notifyObservers("Application status changed for " + applicantName + ": " + status);
    }

    public void newJobPosted(String jobTitle, String companyName) {
        notifyObservers("New job posted: " + jobTitle + " at " + companyName);
    }
}