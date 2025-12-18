package com.jobsphere.model.state;


public interface ApplicationState {

    String getStatusName();
    void nextState(Application application);
    boolean canTransitionTo(String newState);
    String getStateDescription();
    boolean canBeEdited();
    boolean isFinalState();
}
