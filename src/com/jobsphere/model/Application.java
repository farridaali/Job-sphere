package com.jobsphere.model;

import java.util.*;

/**
 * Application model with State Pattern
 * Represents a job application with different states
 */
public class Application {
    private final String id;
    private final String jobId;
    private final String applicantEmail;
    private final String resume;
    private final Date appliedDate;
    private ApplicationState state;

    /**
     * STATE PATTERN
     * Why: Application status transitions follow specific rules
     * Affected: Application status management in ApplicationManagementPanel
     * Benefit: Encapsulates state-specific behavior, prevents invalid transitions
     */
    public interface ApplicationState {
        String getStatusName();
        void nextState(Application application);
        boolean canTransitionTo(String newState);
    }

    public static class PendingState implements ApplicationState {
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

    public static class ReviewingState implements ApplicationState {
        @Override
        public String getStatusName() { return "Reviewing"; }

        @Override
        public void nextState(Application application) {
            application.setState(new InterviewState());
        }

        @Override
        public boolean canTransitionTo(String newState) {
            return newState.equals("Interview") || newState.equals("Rejected");
        }
    }

    public static class InterviewState implements ApplicationState {
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

    public static class AcceptedState implements ApplicationState {
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

    public static class RejectedState implements ApplicationState {
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

    public Application(String jobId, String applicantEmail, String resume) {
        this.id = UUID.randomUUID().toString();
        this.jobId = jobId;
        this.applicantEmail = applicantEmail;
        this.resume = resume;
        this.appliedDate = new Date();
        this.state = new PendingState();
    }

    public String getId() { return id; }
    public String getJobId() { return jobId; }
    public String getApplicantEmail() { return applicantEmail; }
    public String getResume() { return resume; }
    public Date getAppliedDate() { return appliedDate; }
    public String getStatus() { return state.getStatusName(); }

    public void setState(ApplicationState state) {
        this.state = state;
    }

    public void nextState() {
        state.nextState(this);
    }

    public boolean canTransitionTo(String newState) {
        return state.canTransitionTo(newState);
    }

    public void transitionTo(String statusName) {
        switch (statusName) {
            case "Pending":
                setState(new PendingState());
                break;
            case "Reviewing":
                if (state.canTransitionTo("Reviewing"))
                    setState(new ReviewingState());
                break;
            case "Interview":
                if (state.canTransitionTo("Interview"))
                    setState(new InterviewState());
                break;
            case "Accepted":
                if (state.canTransitionTo("Accepted"))
                    setState(new AcceptedState());
                break;
            case "Rejected":
                if (state.canTransitionTo("Rejected"))
                    setState(new RejectedState());
                break;
        }
    }
}