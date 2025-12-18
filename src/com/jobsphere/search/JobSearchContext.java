package com.jobsphere.search;

import com.jobsphere.model.builder.Job;
import java.util.List;
public class JobSearchContext {
    private SearchStrategy strategy;
    public JobSearchContext() {
    }
    public void setStrategy(SearchStrategy strategy) {
        this.strategy = strategy;
    }

    public List<Job> executeSearch(List<Job> jobs, String criteria) {
        if (strategy == null) {
            return jobs;
        }
        return strategy.search(jobs, criteria);
    }
}