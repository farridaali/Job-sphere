package com.jobsphere.model.state;

public  class PendingState extends ApplicationState {
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
}