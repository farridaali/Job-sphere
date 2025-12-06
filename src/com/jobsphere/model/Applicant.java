package com.jobsphere.model;

import java.util.ArrayList;
import java.util.List;

public class Applicant extends User {
    private static final long serialVersionUID = 1L;
    private String name;
    private String phone;
    private String resumePath;
    private List<String> skills;
    private List<String> savedJobIds;
    private List<String> appliedJobIds;

    public Applicant(String email, String password) {
        super(email, password, "APPLICANT");
        this.skills = new ArrayList<>();
        this.savedJobIds = new ArrayList<>();
        this.appliedJobIds = new ArrayList<>();
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getResumePath() { return resumePath; }
    public void setResumePath(String resumePath) { this.resumePath = resumePath; }
    public List<String> getSkills() { return skills; }
    public void setSkills(List<String> skills) { this.skills = skills; }
    public List<String> getSavedJobIds() { return savedJobIds; }
    public List<String> getAppliedJobIds() { return appliedJobIds; }

    public void saveJob(String jobId) {
        if (!savedJobIds.contains(jobId)) {
            savedJobIds.add(jobId);
        }
    }

    public void applyForJob(String jobId) {
        if (!appliedJobIds.contains(jobId)) {
            appliedJobIds.add(jobId);
        }
    }
}