package com.jobsphere.database;

import java.util.*;
import com.jobsphere.model.*;

/*
SINGLETON PATTERN
 */
public class DatabaseManager {
    private static DatabaseManager instance;

    // In-memory storage (simulating database)
    private Map<String,User> users;
    private Map<String,Job> jobs;
    private Map<String, List<Application>> applications;

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
    public void saveUser(User user) {
        users.put(user.getEmail(), user);
    }

    public User getUser(String email) {
        return users.get(email);
    }

    public boolean userExists(String email) {
        return users.containsKey(email);
    }

    // Job operations
    public void saveJob(Job job) {
        jobs.put(job.getId(), job);
    }

    public Job getJob(String jobId) {
        return jobs.get(jobId);
    }

    public List<Job> getAllJobs() {
        return new ArrayList<>(jobs.values());
    }

    public void updateJob(Job job) {
        jobs.put(job.getId(), job);
    }

    public void deleteJob(String jobId) {
        jobs.remove(jobId);
    }

    // Application operations
    public void saveApplication(Application application) {
        String jobId = application.getJobId();
        applications.putIfAbsent(jobId, new ArrayList<>());
        applications.get(jobId).add(application);
    }

    public List<Application> getApplicationsForJob(String jobId) {
        return applications.getOrDefault(jobId, new ArrayList<>());
    }

    public List<Application> getApplicationsForApplicant(String applicantEmail) {
        List<Application> result = new ArrayList<>();
        for (List<Application> appList : applications.values()) {
            for (Application app : appList) {
                if (app.getApplicantEmail().equals(applicantEmail)) {
                    result.add(app);
                }
            }
        }
        return result;
    }

    public void updateApplication(Application application) {
        List<Application> jobApps = applications.get(application.getJobId());
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