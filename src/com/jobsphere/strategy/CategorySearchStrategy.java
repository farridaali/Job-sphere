// CategorySearchStrategy.java
package com.jobsphere.strategy;

import com.jobsphere.model.Job;
import java.util.List;
import java.util.stream.Collectors;

public class CategorySearchStrategy implements JobSearchStrategy {
    @Override
    public List<Job> search(List<Job> jobs, String category) {
        return jobs.stream()
                .filter(job -> job.getCategory() != null &&
                        job.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }
}