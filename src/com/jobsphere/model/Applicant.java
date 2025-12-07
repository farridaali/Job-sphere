package com.jobsphere.model;

import java.util.*;

/**
 * Applicant user type
 * Contains resume, skills, and saved jobs
 */
public class Applicant extends User {
    private String resume;
    private List<String> skills;
    private List<String> savedJobs;

    public Applicant(String email, String password, String name) {
        super(email, password, name, UserType.APPLICANT);
        this.skills = new ArrayList<>();
        this.savedJobs = new ArrayList<>();
    }

    public String getResume() { return resume; }
    public void setResume(String resume) { this.resume = resume; }

    public List<String> getSkills() { return skills; }
    public void addSkill(String skill) { this.skills.add(skill); }

    public List<String> getSavedJobs() { return savedJobs; }
    public void saveJob(String jobId) {
        if (!savedJobs.contains(jobId)) {
            savedJobs.add(jobId);
        }
    }
    public void unsaveJob(String jobId) { savedJobs.remove(jobId); }
    public boolean isJobSaved(String jobId) { return savedJobs.contains(jobId); }

    @Override
    public String getUserDetails() {
        return "Applicant: " + name + " (" + email + ")";
    }
}