package com.jobsphere.database;

import java.util.*;
import com.jobsphere.model.*;
import com.jobsphere.model.builder.Job;
import com.jobsphere.model.state.Application;

public class DatabaseManager {
    private static DatabaseManager instance;

    // el hashmaps ely bt-replicate el database
    private Map<String,User> users;
    private Map<String, Job> jobs;
    private Map<String, List<Application>> applications;

    // ben3ml el constructor private 3shan nemn3 ay 7ad y3ml instance men el object
    private DatabaseManager() {
        users = new HashMap<>();
        jobs = new HashMap<>();
        applications = new HashMap<>();
    }

    // synchronized hena 3shan yab2a thread safe 3shan mafysh thread te3ml instance mo5tlfa 3n el tanya
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public void initialize() {
        System.out.println("Database initialized successfully");
    }

    // Functions el user
    public void saveUser(User user) {
        users.put(user.getEmail(), user);
    }

    public User getUser(String email) {
        return users.get(email);
    }

    public boolean userExists(String email) {
        return users.containsKey(email);
    }

    //end functions el user

    //functions el job
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

    //end functions el job

    // functions el job application
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

    //end functions el applications
}