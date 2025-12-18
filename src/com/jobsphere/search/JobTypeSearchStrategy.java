package com.jobsphere.search;

import com.jobsphere.model.builder.Job;

import java.util.ArrayList;
import java.util.List;

/**
 * Search by job type
 */
public class JobTypeSearchStrategy implements SearchStrategy {
    @Override
    public List<Job> search(List<Job> jobs, String criteria) {
        String type = criteria.toLowerCase();
        List<Job> result = new ArrayList<>();
        for (Job job : jobs) {
            if (job.getJobType() != null &&
                    job.getJobType().toLowerCase().contains(type)) {
                result.add(job);
            }
        }
        return result;
    }
}