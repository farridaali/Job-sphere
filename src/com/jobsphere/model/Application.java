package com.jobsphere.model;

import java.io.Serializable;

public class Application implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String jobId;
    private String applicantId;
    private String status; // PENDING, REVIEWED, ACCEPTED, REJECTED
    private long appliedDate;
    private String resumePath; // Path to uploaded resume

    public Application(String jobId, String applicantId) {
        this.id = generateId();
        this.jobId = jobId;
        this.applicantId = applicantId;
        this.status = "PENDING";
        this.appliedDate = System.currentTimeMillis();
        this.resumePath = "";
    }

    public Application(String jobId, String applicantId, String resumePath) {
        this.id = generateId();
        this.jobId = jobId;
        this.applicantId = applicantId;
        this.status = "PENDING";
        this.appliedDate = System.currentTimeMillis();
        this.resumePath = resumePath;
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
    public String getResumePath() { return resumePath; }
    public void setResumePath(String resumePath) { this.resumePath = resumePath; }
}