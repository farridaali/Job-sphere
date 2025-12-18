package com.jobsphere.model.state;

public class RejectedState implements ApplicationState {
    @Override
    public String getStatusName() { return "Rejected"; }

    @Override
    public void nextState(Application application) {
    }

    @Override
    public boolean canTransitionTo(String newState) {
        return false;
    }

    @Override
    public String getStateDescription() {
        return "Application was not successful at this time";
    }

    @Override
    public boolean canBeEdited() {
        return false;
    }

    @Override
    public boolean isFinalState() {
        return true;
    }
}