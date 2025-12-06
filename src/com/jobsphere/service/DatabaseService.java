package com.jobsphere.service;

import com.jobsphere.model.*;
import com.jobsphere.persistence.DataPersistenceManager;
import java.util.*;
import java.util.stream.Collectors;

/**
 * DESIGN PATTERN: Singleton Pattern
 * Why: Only one database instance should exist throughout the application
 * Affected Classes: DatabaseService
 */
public class DatabaseService {
    private static DatabaseService instance;
    private Map<String, User> users;
    private Map<String, Job> jobs;
    private Map<String, Application> applications;
    private DataPersistenceManager persistenceManager;

    private DatabaseService() {
        persistenceManager = DataPersistenceManager.getInstance();
        loadData();

        // Initialize sample data only if database is empty
        if (users.isEmpty()) {
            initializeSampleData();
            saveData();
        }
    }

    public static synchronized DatabaseService getInstance() {
        if (instance == null) {
            instance = new DatabaseService();
        }
        return instance;
    }

    private void loadData() {
        users = persistenceManager.loadUsers();
        jobs = persistenceManager.loadJobs();
        applications = persistenceManager.loadApplications();
        System.out.println("Data loaded from disk");
    }

    private void saveData() {
        persistenceManager.saveUsers(users);
        persistenceManager.saveJobs(jobs);
        persistenceManager.saveApplications(applications);
    }

    private void initializeSampleData() {
        // Add sample applicant
        Applicant applicant = new Applicant("john@example.com", "password123");
        applicant.setName("John Doe");
        applicant.setPhone("123-456-7890");
        applicant.getSkills().addAll(Arrays.asList("Java", "Python", "SQL"));
        users.put(applicant.getId(), applicant);

        // Add sample company
        Company company = new Company("hr@techcorp.com", "password123");
        company.setCompanyName("TechCorp Inc.");
        company.setIndustry("Technology");
        company.setDescription("Leading software company");
        users.put(company.getId(), company);

        // Add sample jobs
        Job job1 = new Job.Builder(company.getId(), "Senior Java Developer")
                .description("Looking for experienced Java developer")
                .requirements("5+ years Java, Spring Boot, Microservices")
                .location("New York, NY")
                .salary("$120,000 - $150,000")
                .employmentType("Full-time")
                .category("Software Development")
                .build();
        jobs.put(job1.getId(), job1);
        company.addPostedJob(job1.getId());

        Job job2 = new Job.Builder(company.getId(), "Data Analyst")
                .description("Analyze business data and create reports")
                .requirements("SQL, Python, Data Visualization")
                .location("Remote")
                .salary("$80,000 - $100,000")
                .employmentType("Full-time")
                .category("Data Science")
                .build();
        jobs.put(job2.getId(), job2);
        company.addPostedJob(job2.getId());
    }

    // User management
    public User registerUser(User user) {
        users.put(user.getId(), user);
        saveData();
        return user;
    }

    public User authenticateUser(String email, String password) {
        return users.values().stream()
                .filter(u -> u.getEmail().equals(email) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    public User getUserById(String id) {
        return users.get(id);
    }

    public List<Applicant> getAllApplicants() {
        return users.values().stream()
                .filter(u -> u instanceof Applicant)
                .map(u -> (Applicant) u)
                .collect(Collectors.toList());
    }

    // Job management
    public Job addJob(Job job) {
        jobs.put(job.getId(), job);
        saveData();
        return job;
    }

    public Job getJobById(String id) {
        return jobs.get(id);
    }

    public List<Job> getAllJobs() {
        return new ArrayList<>(jobs.values());
    }

    public List<Job> getActiveJobs() {
        return jobs.values().stream()
                .filter(Job::isActive)
                .collect(Collectors.toList());
    }

    public List<Job> searchJobs(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return jobs.values().stream()
                .filter(Job::isActive)
                .filter(job ->
                        job.getTitle().toLowerCase().contains(lowerKeyword) ||
                                job.getDescription().toLowerCase().contains(lowerKeyword) ||
                                job.getCategory().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());
    }

    public void updateJob(Job job) {
        jobs.put(job.getId(), job);
        saveData();
    }

    public void deleteJob(String jobId) {
        jobs.remove(jobId);
        saveData();
    }

    // Application management
    public Application addApplication(Application application) {
        applications.put(application.getId(), application);

        // Update job's applicant list
        Job job = jobs.get(application.getJobId());
        if (job != null) {
            job.addApplicant(application.getApplicantId());
        }

        // Update applicant's applied jobs
        User user = users.get(application.getApplicantId());
        if (user instanceof Applicant) {
            ((Applicant) user).applyForJob(application.getJobId());
        }

        saveData();
        return application;
    }

    public List<Application> getApplicationsByJobId(String jobId) {
        return applications.values().stream()
                .filter(app -> app.getJobId().equals(jobId))
                .collect(Collectors.toList());
    }

    public List<Application> getApplicationsByApplicantId(String applicantId) {
        return applications.values().stream()
                .filter(app -> app.getApplicantId().equals(applicantId))
                .collect(Collectors.toList());
    }

    public void updateApplication(Application application) {
        applications.put(application.getId(), application);
        saveData();
    }

    public boolean hasApplied(String applicantId, String jobId) {
        return applications.values().stream()
                .anyMatch(app -> app.getApplicantId().equals(applicantId) &&
                        app.getJobId().equals(jobId));
    }

    public void updateUser(User user) {
        users.put(user.getId(), user);
        saveData();
    }

    public Application getApplicationById(String applicationId) {
        return applications.get(applicationId);
    }
}