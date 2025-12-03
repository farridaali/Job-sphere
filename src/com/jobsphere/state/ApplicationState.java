// ApplicationState.java
package com.jobsphere.state;

/**
 * DESIGN PATTERN: State Pattern
 * Why: Manages application status transitions with proper state validation
 * Affected Classes: ApplicationState, PendingState, ReviewedState, AcceptedState, RejectedState
 */
public interface ApplicationState {
    void review();
    void accept();
    void reject();
    String getStatus();
}