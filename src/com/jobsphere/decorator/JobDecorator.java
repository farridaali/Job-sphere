package com.jobsphere.decorator;

import com.jobsphere.model.Job;

/**
 * DECORATOR PATTERN (BONUS #5)
 * Why: Add additional features to job listings (featured, urgent, premium)
 * Affected: Job display in search results
 * Benefit: Dynamically add responsibilities without modifying Job class
 */
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

class FeaturedJobDecorator extends JobDecorator {
    public FeaturedJobDecorator(Job job) {
        super(job);
    }

    @Override
    public String getDisplayTitle() {
        return "⭐ FEATURED: " + job.getTitle();
    }

    @Override
    public String getDisplayDescription() {
        return job.getDescription();
    }
}

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