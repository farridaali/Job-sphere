package com.jobsphere.model;

import java.util.*;

/**
 * BUILDER PATTERN
 * Why: Job has many optional fields, Builder makes construction cleaner
 * Affected: Job creation in JobPostingPanel
 * Benefit: Flexible object construction, readable code, immutable objects
 */
public class Job {
    private final String id;
    private final String companyEmail;
    private final String title;
    private final String description;
    private final String location;
    private final String jobType;
    private final String salary;
    private final List<String> requirements;
    private final List<String> responsibilities;
    private JobStatus status;

    public enum JobStatus {
        ACTIVE, PAUSED, CLOSED
    }

    private Job(JobBuilder builder) {
        this.id = builder.id;
        this.companyEmail = builder.companyEmail;
        this.title = builder.title;
        this.description = builder.description;
        this.location = builder.location;
        this.jobType = builder.jobType;
        this.salary = builder.salary;
        this.requirements = builder.requirements;
        this.responsibilities = builder.responsibilities;
        this.status = builder.status;
    }

    // Getters
    public String getId() { return id; }
    public String getCompanyEmail() { return companyEmail; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public String getJobType() { return jobType; }
    public String getSalary() { return salary; }
    public List<String> getRequirements() { return requirements; }
    public List<String> getResponsibilities() { return responsibilities; }
    public JobStatus getStatus() { return status; }
    public void setStatus(JobStatus status) { this.status = status; }

    /**
     * Builder class for Job
     */
    public static class JobBuilder {
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

        public JobBuilder(String companyEmail, String title) {
            this.id = UUID.randomUUID().toString();
            this.companyEmail = companyEmail;
            this.title = title;
            this.requirements = new ArrayList<>();
            this.responsibilities = new ArrayList<>();
            this.status = JobStatus.ACTIVE;
        }

        public JobBuilder description(String description) {
            this.description = description;
            return this;
        }

        public JobBuilder location(String location) {
            this.location = location;
            return this;
        }

        public JobBuilder jobType(String jobType) {
            this.jobType = jobType;
            return this;
        }

        public JobBuilder salary(String salary) {
            this.salary = salary;
            return this;
        }

        public JobBuilder requirements(List<String> requirements) {
            this.requirements = requirements;
            return this;
        }

        public JobBuilder responsibilities(List<String> responsibilities) {
            this.responsibilities = responsibilities;
            return this;
        }

        public JobBuilder status(JobStatus status) {
            this.status = status;
            return this;
        }

        public Job build() {
            return new Job(this);
        }
    }
}