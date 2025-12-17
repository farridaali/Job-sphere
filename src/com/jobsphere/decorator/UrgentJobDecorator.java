package com.jobsphere.decorator;

import com.jobsphere.model.Job;

class UrgentJobDecorator extends JobDecorator {
    public UrgentJobDecorator(Job job) {
        super(job);
    }

    @Override
    public String getDisplayTitle() {
        return "🔥 URGENT: " + job.getTitle();
    }

    @Override
    public String getDisplayDescription() {
        return "URGENT HIRING - " + job.getDescription();
    }
}
