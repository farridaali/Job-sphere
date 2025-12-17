package com.jobsphere.model;

import java.util.*;

public class Company extends User {
    private String companyDescription;
    private String industry;
    private List<String> postedJobs;

    public Company(String email, String password, String name) {
        super(email, password, name, UserType.COMPANY);
        this.postedJobs = new ArrayList<>();
    }

    public String getCompanyDescription() { return companyDescription; }
    public void setCompanyDescription(String description) { this.companyDescription = description; }

    public String getIndustry() { return industry; }
    public void setIndustry(String industry) { this.industry = industry; }

    public List<String> getPostedJobs() { return postedJobs; }
    public void addPostedJob(String jobId) { this.postedJobs.add(jobId); }
    public void removePostedJob(String jobId) { this.postedJobs.remove(jobId); }

    @Override
    public String getUserDetails() {
        return "Company: " + name + " (" + email + ")";
    }
}