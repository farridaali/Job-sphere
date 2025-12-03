// AcceptedState.java
package com.jobsphere.state;

public class AcceptedState implements ApplicationState {
    private ApplicationContext context;

    public AcceptedState(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void review() {
        System.out.println("Cannot review accepted application");
    }

    @Override
    public void accept() {
        System.out.println("Application already accepted");
    }

    @Override
    public void reject() {
        System.out.println("Cannot reject accepted application");
    }

    @Override
    public String getStatus() {
        return "ACCEPTED";
    }
}