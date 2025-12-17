package com.jobsphere.model;
public abstract class User {
    protected String email;
    protected String password;
    protected String name;
    protected UserType userType;

    public enum UserType {
        APPLICANT, COMPANY
    }

    public User(String email, String password, String name, UserType userType) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.userType = userType;
    }

    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getName() { return name; }
    public UserType getUserType() { return userType; }

    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setName(String name) { this.name = name; }

    public abstract String getUserDetails();
}