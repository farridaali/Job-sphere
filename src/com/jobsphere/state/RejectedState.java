package com.jobsphere.state;

public class RejectedState implements ApplicationState {
    private ApplicationContext context;

    public RejectedState(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void review() {
        System.out.println("Cannot review rejected application");
    }

    @Override
    public void accept() {
        System.out.println("Cannot accept rejected application");
    }

    @Override
    public void reject() {
        System.out.println("Application already rejected");
    }

    @Override
    public String getStatus() {
        return "REJECTED";
    }
}