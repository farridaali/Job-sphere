package com.jobsphere.model;

public  class RejectedState extends  ApplicationState {
    @Override
    public String getStatusName() { return "Rejected"; }

    @Override
    public void nextState(Application application) {
        // Final state
    }

    @Override
    public boolean canTransitionTo(String newState) {
        return false; // Terminal state
    }
}