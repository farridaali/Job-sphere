package com.jobsphere.model.state;

public  class AcceptedState extends ApplicationState {
    @Override
    public String getStatusName() { return "Accepted"; }

    @Override
    public void nextState(Application application) {
        // Final state
    }

    @Override
    public boolean canTransitionTo(String newState) {
        return false; // Terminal state
    }
}