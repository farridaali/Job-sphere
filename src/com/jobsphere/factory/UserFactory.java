package com.jobsphere.factory;

import com.jobsphere.model.Applicant;
import com.jobsphere.model.Company;
import com.jobsphere.model.User;

/**
 * DESIGN PATTERN: Factory Pattern
 * Why: Encapsulates user creation logic based on user type
 * Affected Classes: UserFactory
 */
public class UserFactory {

    public static User createUser(String userType, String email, String password) {
        if (userType == null || userType.isEmpty()) {
            throw new IllegalArgumentException("User type cannot be null or empty");
        }

        switch (userType.toUpperCase()) {
            case "APPLICANT":
                return new Applicant(email, password);
            case "COMPANY":
                return new Company(email, password);
            default:
                throw new IllegalArgumentException("Unknown user type: " + userType);
        }
    }
}