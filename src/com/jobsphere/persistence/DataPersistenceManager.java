package com.jobsphere.persistence;

import com.jobsphere.model.*;
import java.io.*;
import java.util.*;

/**
 * DESIGN PATTERN: Singleton Pattern (extends the pattern to persistence)
 * Why: Ensures single point of file I/O operations, prevents concurrent file access issues
 * Affected Classes: DataPersistenceManager
 */
public class DataPersistenceManager {
    private static DataPersistenceManager instance;
    private static final String DATA_DIR = "jobsphere_data";
    private static final String USERS_FILE = DATA_DIR + "/users.dat";
    private static final String JOBS_FILE = DATA_DIR + "/jobs.dat";
    private static final String APPLICATIONS_FILE = DATA_DIR + "/applications.dat";

    private DataPersistenceManager() {
        createDataDirectory();
    }

    public static synchronized DataPersistenceManager getInstance() {
        if (instance == null) {
            instance = new DataPersistenceManager();
        }
        return instance;
    }

    private void createDataDirectory() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    // Save users to file
    public void saveUsers(Map<String, User> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
            oos.writeObject(users);
            System.out.println("Users saved successfully");
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }

    // Load users from file
    @SuppressWarnings("unchecked")
    public Map<String, User> loadUsers() {
        File file = new File(USERS_FILE);
        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USERS_FILE))) {
            return (Map<String, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading users: " + e.getMessage());
            return new HashMap<>();
        }
    }

    // Save jobs to file
    public void saveJobs(Map<String, Job> jobs) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(JOBS_FILE))) {
            oos.writeObject(jobs);
            System.out.println("Jobs saved successfully");
        } catch (IOException e) {
            System.err.println("Error saving jobs: " + e.getMessage());
        }
    }

    // Load jobs from file
    @SuppressWarnings("unchecked")
    public Map<String, Job> loadJobs() {
        File file = new File(JOBS_FILE);
        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(JOBS_FILE))) {
            return (Map<String, Job>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading jobs: " + e.getMessage());
            return new HashMap<>();
        }
    }

    // Save applications to file
    public void saveApplications(Map<String, Application> applications) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(APPLICATIONS_FILE))) {
            oos.writeObject(applications);
            System.out.println("Applications saved successfully");
        } catch (IOException e) {
            System.err.println("Error saving applications: " + e.getMessage());
        }
    }

    // Load applications from file
    @SuppressWarnings("unchecked")
    public Map<String, Application> loadApplications() {
        File file = new File(APPLICATIONS_FILE);
        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(APPLICATIONS_FILE))) {
            return (Map<String, Application>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading applications: " + e.getMessage());
            return new HashMap<>();
        }
    }

    // Clear all data (for testing)
    public void clearAllData() {
        new File(USERS_FILE).delete();
        new File(JOBS_FILE).delete();
        new File(APPLICATIONS_FILE).delete();
        System.out.println("All data cleared");
    }
}