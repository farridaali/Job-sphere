package com.jobsphere.model;

import java.io.Serializable;

/**
 * DESIGN PATTERN: Prototype Pattern
 * Why: Allows cloning of user objects for creating similar profiles
 * Affected Classes: User, Applicant, Company
 */
public abstract class User implements Cloneable, Serializable {
    private static final long serialVersionUID = 1L;
    protected String id;
    protected String email;
    protected String password;
    protected String userType;

    public User(String email, String password, String userType) {
        this.id = generateId();
        this.email = email;
        this.password = password;
        this.userType = userType;
    }

    private String generateId() {
        return "USER_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
    }

    @Override
    public User clone() {
        try {
            return (User) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Clone not supported", e);
        }
    }

    // Getters and setters
    public String getId() { return id; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getUserType() { return userType; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
}