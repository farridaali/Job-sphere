package com.jobsphere.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * DESIGN PATTERN: Builder Pattern
 * Why: Job objects have many optional fields, Builder makes construction cleaner
 * Affected Classes: Job, Job.Builder
 */
public class Job implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String companyId;
    private String title;
    private String description;
    private String requirements;
    private String location;
    private String salary;
    private String employmentType;
    private String category;
    private boolean isActive;
    private long postedDate;
    private List<String> applicantIds;

    private Job(Builder builder) {
        this.id = generateId();
        this.companyId = builder.companyId;
        this.title = builder.title;
        this.description = builder.description;
        this.requirements = builder.requirements;
        this.location = builder.location;
        this.salary = builder.salary;
        this.employmentType = builder.employmentType;
        this.category = builder.category;
        this.isActive = true;
        this.postedDate = System.currentTimeMillis();
        this.applicantIds = new ArrayList<>();
    }

    private String generateId() {
        return "JOB_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
    }

    // Getters
    public String getId() { return id; }
    public String getCompanyId() { return companyId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getRequirements() { return requirements; }
    public String getLocation() { return location; }
    public String getSalary() { return salary; }
    public String getEmploymentType() { return employmentType; }
    public String getCategory() { return category; }
    public boolean isActive() { return isActive; }
    public long getPostedDate() { return postedDate; }
    public List<String> getApplicantIds() { return applicantIds; }

    // Setters for mutable fields
    public void setActive(boolean active) { isActive = active; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setRequirements(String requirements) { this.requirements = requirements; }
    public void setSalary(String salary) { this.salary = salary; }

    public void addApplicant(String applicantId) {
        if (!applicantIds.contains(applicantId)) {
            applicantIds.add(applicantId);
        }
    }

    /**
     * Builder class for constructing Job objects
     */
    public static class Builder {
        private String companyId;
        private String title;
        private String description;
        private String requirements;
        private String location;
        private String salary;
        private String employmentType;
        private String category;

        public Builder(String companyId, String title) {
            this.companyId = companyId;
            this.title = title;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder requirements(String requirements) {
            this.requirements = requirements;
            return this;
        }

        public Builder location(String location) {
            this.location = location;
            return this;
        }

        public Builder salary(String salary) {
            this.salary = salary;
            return this;
        }

        public Builder employmentType(String employmentType) {
            this.employmentType = employmentType;
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Job build() {
            return new Job(this);
        }
    }
}