package com.jobsphere.state;

public class ReviewedState implements ApplicationState {
    private ApplicationContext context;

    public ReviewedState(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void review() {
        System.out.println("Application already reviewed");
    }

    @Override
    public void accept() {
        System.out.println("Application ACCEPTED");
        context.setState(new AcceptedState(context));
    }

    @Override
    public void reject() {
        System.out.println("Application REJECTED");
        context.setState(new RejectedState(context));
    }

    @Override
    public String getStatus() {
        return "REVIEWED";
    }
}