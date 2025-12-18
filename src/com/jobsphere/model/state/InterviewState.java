package com.jobsphere.model.state;

public  class InterviewState extends ApplicationState {
    @Override
    public String getStatusName() { return "Interview"; }

    @Override
    public void nextState(Application application) {
        application.setState(new AcceptedState());
    }

    @Override
    public boolean canTransitionTo(String newState) {
        return newState.equals("Accepted") || newState.equals("Rejected");
    }
}