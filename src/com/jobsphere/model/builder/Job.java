package com.jobsphere.model.builder;

import java.util.*;


public class Job {
    private String id;
    private String companyEmail;
    private String title;
    private String description;
    private String location;
    private String jobType;
    private String salary;
    private List<String> requirements;
    private List<String> responsibilities;
    private JobStatus status;

    public enum JobStatus {
        ACTIVE, PAUSED, CLOSED
    }

    public Job(String description, String title, String companyEmail) {
        this.id = UUID.randomUUID().toString();
        this.companyEmail = companyEmail;
        this.description = description;
        this.title = title;
        this.status = JobStatus.ACTIVE;  // Default status
    }

    public String getId() {
        return id;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getJobType() {
        return jobType;
    }

    public String getSalary() {
        return salary;
    }

    public List<String> getRequirements() {
        return requirements;
    }

    public List<String> getResponsibilities() {
        return responsibilities;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public void setRequirements(List<String> requirements) {
        this.requirements = requirements;
    }


    public void setResponsibilities(List<String> responsibilities) {
        this.responsibilities = responsibilities;
    }


    public void setStatus(JobStatus status) {
        this.status = status;
    }


    public boolean canBeEdited() {
        return this.status != JobStatus.CLOSED;
    }

    @Override
    public String toString() {
        return "Job{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", companyEmail='" + companyEmail + '\'' +
                ", status=" + status +
                '}';
    }
}