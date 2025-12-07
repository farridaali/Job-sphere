package com.jobsphere.factory;

import com.jobsphere.model.*;

/**
 * FACTORY PATTERN
 * Why: Creates different user types (Applicant/Company) without exposing creation logic
 * Affected: User registration and login processes
 * Benefit: Centralizes user creation, easy to add new user types
 */
public class UserFactory {

    public static User createUser(User.UserType type, String email, String password, String name) {
        switch (type) {
            case APPLICANT:
                return new Applicant(email, password, name);
            case COMPANY:
                return new Company(email, password, name);
            default:
                throw new IllegalArgumentException("Unknown user type: " + type);
        }
    }
}