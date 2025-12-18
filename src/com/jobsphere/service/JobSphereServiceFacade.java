package com.jobsphere.service;

import com.jobsphere.database.DatabaseManager;
import com.jobsphere.factory.UserFactory;
import com.jobsphere.model.*;
import com.jobsphere.model.builder.Job;
import com.jobsphere.model.state.Application;
import java.util.List;

/**
 * Ashl pattern fyhom dy bt3ml interface lel ui eno y-access kol el classes ely 3amlnha
 * men 8er man5osh fy tafsayl wel functionalities bt3t el classes
 * we btkon zy orchestrator keda 3shan heya betnazem el interaction maben el classes
 * 3shan newsal lel end goal bt3na
 *
 * nafs fekrt el api calls 3amatan
 * zy maslan fy website lawo 3ayz a3ml get aw post lel server msh lazem
 * a3ml el request fel ui we al5bt el denya ana bas b-call function we ary7 dem8y
 * we akon 3aml el function fy class ba2a mag3las el ui malosh da3wa by
 *
 * we bt3ml bardo be-fekrt el singleton ely fy instance wa7da le interface 3shan
 * a-access kol ely ana 3ayzo men el system
 */
public class JobSphereServiceFacade {
    private static JobSphereServiceFacade instance;
    private DatabaseManager dbManager;

    private JobSphereServiceFacade() {
        this.dbManager = DatabaseManager.getInstance();
    }

    public static synchronized JobSphereServiceFacade getInstance() {
        if (instance == null) {
            instance = new JobSphereServiceFacade();
        }
        return instance;
    }

    // Functionality el Users
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

    //end Functionality el Users

    //  Functionality el Job
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

    public void updateJob(Job job) {
        dbManager.updateJob(job);
    }

    public void deleteJob(String jobId, Company company) {
        dbManager.deleteJob(jobId);
        company.removePostedJob(jobId);
        dbManager.saveUser(company);
    }

    // End Functionality el Job

    // Functionality el Application
    public void submitApplication(Application application, String jobTitle, String companyEmail) {
        dbManager.saveApplication(application);
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
    }
    // end Functionality el application

    // Functionality en e7na ne-save job aw n-unsave el job
    public void saveJobForApplicant(Applicant applicant, String jobId) {
        applicant.saveJob(jobId);
        dbManager.saveUser(applicant);
    }

    public void unsaveJobForApplicant(Applicant applicant, String jobId) {
        applicant.unsaveJob(jobId);
        dbManager.saveUser(applicant);
    }
    // end lyha
}