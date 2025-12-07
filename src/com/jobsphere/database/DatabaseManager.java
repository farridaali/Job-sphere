package com.jobsphere.database;

import java.util.*;

/**
 * SINGLETON PATTERN
 * Why: Ensures only one database connection instance exists throughout the application
 * Affected: All classes that need database access
 * Benefit: Prevents multiple connections, ensures data consistency
 */
public class DatabaseManager {
    private static DatabaseManager instance;

    // In-memory storage (simulating database)
    private Map<String, com.jobsphere.model.User> users;
    private Map<String, com.jobsphere.model.Job> jobs;
    private Map<String, List<com.jobsphere.model.Application>> applications;

    // Private constructor prevents instantiation
    private DatabaseManager() {
        users = new HashMap<>();
        jobs = new HashMap<>();
        applications = new HashMap<>();
    }

    // Thread-safe singleton instance
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public void initialize() {
        System.out.println("Database initialized successfully");
    }

    // User operations
    public void saveUser(com.jobsphere.model.User user) {
        users.put(user.getEmail(), user);
    }

    public com.jobsphere.model.User getUser(String email) {
        return users.get(email);
    }

    public boolean userExists(String email) {
        return users.containsKey(email);
    }

    // Job operations
    public void saveJob(com.jobsphere.model.Job job) {
        jobs.put(job.getId(), job);
    }

    public com.jobsphere.model.Job getJob(String jobId) {
        return jobs.get(jobId);
    }

    public List<com.jobsphere.model.Job> getAllJobs() {
        return new ArrayList<>(jobs.values());
    }

    public void updateJob(com.jobsphere.model.Job job) {
        jobs.put(job.getId(), job);
    }

    public void deleteJob(String jobId) {
        jobs.remove(jobId);
    }

    // Application operations
    public void saveApplication(com.jobsphere.model.Application application) {
        String jobId = application.getJobId();
        applications.putIfAbsent(jobId, new ArrayList<>());
        applications.get(jobId).add(application);
    }

    public List<com.jobsphere.model.Application> getApplicationsForJob(String jobId) {
        return applications.getOrDefault(jobId, new ArrayList<>());
    }

    public List<com.jobsphere.model.Application> getApplicationsForApplicant(String applicantEmail) {
        List<com.jobsphere.model.Application> result = new ArrayList<>();
        for (List<com.jobsphere.model.Application> appList : applications.values()) {
            for (com.jobsphere.model.Application app : appList) {
                if (app.getApplicantEmail().equals(applicantEmail)) {
                    result.add(app);
                }
            }
        }
        return result;
    }

    public void updateApplication(com.jobsphere.model.Application application) {
        List<com.jobsphere.model.Application> jobApps = applications.get(application.getJobId());
        if (jobApps != null) {
            for (int i = 0; i < jobApps.size(); i++) {
                if (jobApps.get(i).getId().equals(application.getId())) {
                    jobApps.set(i, application);
                    break;
                }
            }
        }
    }
}