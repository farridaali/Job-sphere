package com.jobsphere.model;

import java.util.ArrayList;
import java.util.List;

public class Company extends User {
    private static final long serialVersionUID = 1L;
    private String companyName;
    private String industry;
    private String description;
    private List<String> postedJobIds;

    public Company(String email, String password) {
        super(email, password, "COMPANY");
        this.postedJobIds = new ArrayList<>();
    }

    // Getters and setters
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getIndustry() { return industry; }
    public void setIndustry(String industry) { this.industry = industry; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<String> getPostedJobIds() { return postedJobIds; }

    public void addPostedJob(String jobId) {
        postedJobIds.add(jobId);
    }

    public void removePostedJob(String jobId) {
        postedJobIds.remove(jobId);
    }
}