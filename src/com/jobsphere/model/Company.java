package com.jobsphere.model;

import java.util.*;

public class Company extends User {
    private List<String> postedJobs;

    public Company(String email, String password, String name) {
        super(email, password, name, UserType.COMPANY);
        this.postedJobs = new ArrayList<>();
    }


    public List<String> getPostedJobs() { return postedJobs; }
    public void addPostedJob(String jobId) { this.postedJobs.add(jobId); }
    public void removePostedJob(String jobId) { this.postedJobs.remove(jobId); }

}