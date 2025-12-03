// ApplicationContext.java
package com.jobsphere.state;

public class ApplicationContext {
    private ApplicationState currentState;

    public ApplicationContext() {
        this.currentState = new PendingState(this);
    }

    public void setState(ApplicationState state) {
        this.currentState = state;
    }

    public void review() {
        currentState.review();
    }

    public void accept() {
        currentState.accept();
    }

    public void reject() {
        currentState.reject();
    }

    public String getStatus() {
        return currentState.getStatus();
    }
}