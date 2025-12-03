package com.jobsphere.model;

public class Application {
    private String id;
    private String jobId;
    private String applicantId;
    private String status; // PENDING, REVIEWED, ACCEPTED, REJECTED
    private long appliedDate;

    public Application(String jobId, String applicantId) {
        this.id = generateId();
        this.jobId = jobId;
        this.applicantId = applicantId;
        this.status = "PENDING";
        this.appliedDate = System.currentTimeMillis();
    }

    private String generateId() {
        return "APP_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
    }

    // Getters and setters
    public String getId() { return id; }
    public String getJobId() { return jobId; }
    public String getApplicantId() { return applicantId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public long getAppliedDate() { return appliedDate; }
}