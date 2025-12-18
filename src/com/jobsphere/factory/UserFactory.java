package com.jobsphere.factory;

import com.jobsphere.model.*;

/**
 Dy simple factory bt5aly el client y-create ay no3 user howa 3ayzo
 men 8er maysh8l dem8o bel implementation bt3o

 badel ma-nfdal ne3ml User user = new Applicant fy kol 7eta han3ml fyha user
 bn-call bas function us el create user men object el userfactory( Decoupling)
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