package com.jobsphere.model.state;

import java.util.*;


/**
 fel application betro7 le kaza mar7la(state) we ma3 ta8yor kol mar7ala
 fy functionalities btet3ml 3ala 7aseb el el mar7ala ely e7na fyha we
 byb2a fy nezam le el states ya3ny fel reviewing maynf3sh nenot 3al accepted 3shan
 mynf3sh applicant yenot y3ady men 7etet el acceptance 8er lama ya3dy 3al interview
 fy 5 mara7al (pending, reviewing, interview,accepted, rejected)
 3shan newsal lel acceptance lazem ne3dy 3ala kol ely ablo
 lakan momken nero7 men ay mar7la lel rejected 3alatoul
 */

public class Application {
    private final String id;
    private final String jobId;
    private final String applicantEmail;
    private final String resume;
    private final Date appliedDate;
    private ApplicationState state;

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