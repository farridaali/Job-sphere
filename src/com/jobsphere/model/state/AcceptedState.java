package com.jobsphere.model.state;

public class AcceptedState implements ApplicationState {
    @Override
    public String getStatusName() { return "Accepted"; }

    @Override
    public void nextState(Application application) {
        // Final state - no next state
    }

    @Override
    public boolean canTransitionTo(String newState) {
        return false;
    }

    @Override
    public String getStateDescription() {
        return "Congratulations! Application accepted";
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