// JobManagementFacade.java
package com.jobsphere.facade;

import com.jobsphere.model.*;
import com.jobsphere.service.DatabaseService;

import java.util.List;

/**
 * DESIGN PATTERN: Facade Pattern
 * Why: Provides simplified interface for complex job management operations
 * Affected Classes: JobManagementFacade
 */
public class JobManagementFacade {
    private DatabaseService database;

    public JobManagementFacade() {
        this.database = DatabaseService.getInstance();
    }

    // Applicant operations
    public List<Job> searchJobs(String keyword) {
        return database.searchJobs(keyword);
    }

    public List<Job> getAllActiveJobs() {
        return database.getActiveJobs();
    }

    public Job getJobDetails(String jobId) {
        return database.getJobById(jobId);
    }

    public boolean applyForJob(String applicantId, String jobId) {
        if (database.hasApplied(applicantId, jobId)) {
            return false; // Already applied
        }

        Application application = new Application(jobId, applicantId);
        database.addApplication(application);
        return true;
    }

    public void saveJob(String applicantId, String jobId) {
        User user = database.getUserById(applicantId);
        if (user instanceof Applicant) {
            ((Applicant) user).saveJob(jobId);
        }
    }

    public List<String> getSavedJobs(String applicantId) {
        User user = database.getUserById(applicantId);
        if (user instanceof Applicant) {
            return ((Applicant) user).getSavedJobIds();
        }
        return null;
    }

    public List<Application> getApplicantApplications(String applicantId) {
        return database.getApplicationsByApplicantId(applicantId);
    }

    // Company operations
    public Job postJob(Job job) {
        Job postedJob = database.addJob(job);

        // Update company's posted jobs list
        User user = database.getUserById(job.getCompanyId());
        if (user instanceof Company) {
            ((Company) user).addPostedJob(postedJob.getId());
        }

        return postedJob;
    }

    public void updateJob(Job job) {
        database.updateJob(job);
    }

    public void deleteJob(String jobId, String companyId) {
        database.deleteJob(jobId);

        // Remove from company's posted jobs
        User user = database.getUserById(companyId);
        if (user instanceof Company) {
            ((Company) user).removePostedJob(jobId);
        }
    }

    public void toggleJobStatus(String jobId) {
        Job job = database.getJobById(jobId);
        if (job != null) {
            job.setActive(!job.isActive());
            database.updateJob(job);
        }
    }

    public List<Application> getJobApplications(String jobId) {
        return database.getApplicationsByJobId(jobId);
    }

    public void updateApplicationStatus(String applicationId, String status) {
        Application app = database.getApplicationsByJobId("").stream()
                .filter(a -> a.getId().equals(applicationId))
                .findFirst()
                .orElse(null);

        if (app != null) {
            app.setStatus(status);
            database.updateApplication(app);
        }
    }

    public Applicant getApplicantById(String applicantId) {
        User user = database.getUserById(applicantId);
        return (user instanceof Applicant) ? (Applicant) user : null;
    }

    public List<Applicant> searchCandidates(String keyword) {
        List<Applicant> allApplicants = database.getAllApplicants();
        String lowerKeyword = keyword.toLowerCase();

        return allApplicants.stream()
                .filter(applicant ->
                        applicant.getName().toLowerCase().contains(lowerKeyword) ||
                                applicant.getSkills().stream().anyMatch(s -> s.toLowerCase().contains(lowerKeyword)))
                .collect(java.util.stream.Collectors.toList());
    }
}