package com.jobsphere.decorator;

import com.jobsphere.model.Job;
public abstract class JobDecorator {
    protected Job job;

    public JobDecorator(Job job) {
        this.job = job;
    }

    public abstract String getDisplayTitle();
    public abstract String getDisplayDescription();

    public Job getJob() {
        return job;
    }
}