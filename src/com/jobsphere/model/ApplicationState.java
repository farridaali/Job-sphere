package com.jobsphere.model;

public abstract class ApplicationState {

    //protected Application application;
    public abstract String  getStatusName();
    public abstract void  nextState(Application application);
    public abstract boolean  canTransitionTo(String newState);
}
