package com.jobsphere.model.state;

public class PendingState implements ApplicationState {
    @Override
    public String getStatusName() { return "Pending"; }

    @Override
    public void nextState(Application application) {
        application.setState(new ReviewingState());
    }

    @Override
    public boolean canTransitionTo(String newState) {
        return newState.equals("Reviewing") || newState.equals("Rejected");
    }

    @Override
    public String getStateDescription() {
        return "Application received and awaiting initial review";
    }

    @Override
    public boolean canBeEdited() {
        return true;
    }

    @Override
    public boolean isFinalState() {
        return false;
    }
}