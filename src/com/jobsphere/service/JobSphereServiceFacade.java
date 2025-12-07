package com.jobsphere.service;

import com.jobsphere.database.DatabaseManager;
import com.jobsphere.factory.UserFactory;
import com.jobsphere.model.*;
import com.jobsphere.notification.NotificationManager;
import java.util.List;

/**
 * FACADE PATTERN (BONUS #6)
 * Why: Simplifies complex subsystem interactions (database, notifications, validation)
 * Affected: All UI panels that need business logic
 * Benefit: Single entry point for business operations, hides complexity
 */
public class JobSphereServiceFacade {
    private static JobSphereServiceFacade instance;
    private DatabaseManager dbManager;
    private NotificationManager notificationManager;

    private JobSphereServiceFacade() {
        this.dbManager = DatabaseManager.getInstance();
        this.notificationManager = NotificationManager.getInstance();
    }

    public static synchronized JobSphereServiceFacade getInstance() {
        if (instance == null) {
            instance = new JobSphereServiceFacade();
        }
        return instance;
    }

    // User operations
    public User registerUser(User.UserType type, String email, String password, String name) {
        if (dbManager.userExists(email)) {
            return null;
        }
        User user = UserFactory.createUser(type, email, password, name);
        dbManager.saveUser(user);
        return user;
    }

    public User loginUser(String email, String password) {
        User user = dbManager.getUser(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    // Job operations
    public void postJob(Job job, Company company) {
        dbManager.saveJob(job);
        company.addPostedJob(job.getId());
        dbManager.saveUser(company);
    }

    public List<Job> searchJobs() {
        return dbManager.getAllJobs();
    }

    public Job getJob(String jobId) {
        return dbManager.getJob(jobId);
    }

    public void updateJobStatus(String jobId, Job.JobStatus status) {
        Job job = dbManager.getJob(jobId);
        if (job != null) {
            job.setStatus(status);
            dbManager.updateJob(job);
        }
    }

    // Application operations
    public void submitApplication(Application application, String jobTitle, String companyEmail) {
        dbManager.saveApplication(application);
        notificationManager.notifyNewApplication(
                companyEmail,
                application.getApplicantEmail(),
                jobTitle
        );
    }

    public List<Application> getApplicationsForJob(String jobId) {
        return dbManager.getApplicationsForJob(jobId);
    }

    public List<Application> getApplicationsForApplicant(String applicantEmail) {
        return dbManager.getApplicationsForApplicant(applicantEmail);
    }

    public void updateApplicationStatus(Application application, String newStatus, String jobTitle) {
        application.transitionTo(newStatus);
        dbManager.updateApplication(application);
        notificationManager.notifyApplicationStatusChange(
                application.getApplicantEmail(),
                jobTitle,
                newStatus
        );
    }

    // Saved jobs
    public void saveJobForApplicant(Applicant applicant, String jobId) {
        applicant.saveJob(jobId);
        dbManager.saveUser(applicant);
    }

    public void unsaveJobForApplicant(Applicant applicant, String jobId) {
        applicant.unsaveJob(jobId);
        dbManager.saveUser(applicant);
    }
}