package com.jobsphere.search;

import com.jobsphere.model.Job;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Search by job type
 */
public class JobTypeSearchStrategy implements SearchStrategy {
    @Override
    public List<Job> search(List<Job> jobs, String criteria) {
        String type = criteria.toLowerCase();
        return jobs.stream()
                .filter(job -> job.getJobType() != null &&
                        job.getJobType().toLowerCase().contains(type))
                .collect(Collectors.toList());
    }
}