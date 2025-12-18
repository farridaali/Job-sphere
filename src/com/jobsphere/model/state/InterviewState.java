package com.jobsphere.model.state;

public class InterviewState implements ApplicationState {
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

    @Override
    public String getStateDescription() {
        return "Candidate scheduled for interview";
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