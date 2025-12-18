package com.jobsphere.model.state;

public class ReviewingState implements ApplicationState {
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

    @Override
    public String getStateDescription() {
        return "Application is under review by hiring team";
    }

    @Override
    public boolean canBeEdited() {
        return false;
    }

    @Override
    public boolean isFinalState() {
        return false;
    }
}