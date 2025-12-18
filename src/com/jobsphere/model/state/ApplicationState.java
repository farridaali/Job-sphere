package com.jobsphere.model.state;


public abstract class ApplicationState {

    public abstract String  getStatusName();
    public abstract void  nextState(Application application);
    public abstract boolean  canTransitionTo(String newState);
}
