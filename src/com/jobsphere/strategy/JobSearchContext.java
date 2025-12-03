// JobSearchContext.java
package com.jobsphere.strategy;

import com.jobsphere.model.Job;
import java.util.List;

public class JobSearchContext {
    private JobSearchStrategy strategy;

    public void setStrategy(JobSearchStrategy strategy) {
        this.strategy = strategy;
    }

    public List<Job> executeSearch(List<Job> jobs, String criteria) {
        if (strategy == null) {
            throw new IllegalStateException("Search strategy not set");
        }
        return strategy.search(jobs, criteria);
    }
}