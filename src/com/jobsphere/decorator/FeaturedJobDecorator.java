package com.jobsphere.decorator;

import com.jobsphere.model.Job;

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