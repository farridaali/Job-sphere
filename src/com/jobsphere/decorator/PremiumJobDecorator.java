package com.jobsphere.decorator;

import com.jobsphere.model.Job;

class PremiumJobDecorator extends JobDecorator {
    public PremiumJobDecorator(Job job) {
        super(job);
    }

    @Override
    public String getDisplayTitle() {
        return "💎 PREMIUM: " + job.getTitle();
    }

    @Override
    public String getDisplayDescription() {
        return job.getDescription() + "\n\nPremium employer - Fast hiring process!";
    }
}
