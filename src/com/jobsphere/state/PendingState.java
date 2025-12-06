package com.jobsphere.state;

public class PendingState implements ApplicationState {
    private ApplicationContext context;

    public PendingState(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void review() {
        System.out.println("Application moved to REVIEWED");
        context.setState(new ReviewedState(context));
    }

    @Override
    public void accept() {
        System.out.println("Cannot accept application without review");
    }

    @Override
    public void reject() {
        System.out.println("Application REJECTED");
        context.setState(new RejectedState(context));
    }

    @Override
    public String getStatus() {
        return "PENDING";
    }
}