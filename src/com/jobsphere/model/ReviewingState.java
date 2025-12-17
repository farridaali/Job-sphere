package com.jobsphere.model;

public  class ReviewingState extends ApplicationState {
    @Override
    public String getStatusName() { return "Reviewing"; }

    @Override
    public void nextState(Application application) {
        application.setState(new InterviewState());
    }

    @Override
    public boolean canTransitionTo(String newState) {
        return newState.equals("Interview") || newState.equals("Rejected");
    }
}