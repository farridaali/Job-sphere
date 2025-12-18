package com.jobsphere.search;

import com.jobsphere.model.builder.Job;

import java.util.ArrayList;
import java.util.List;

/**
 * Search by location
 */
public class LocationSearchStrategy implements SearchStrategy {
    @Override
    public List<Job> search(List<Job> jobs, String criteria) {
        String location = criteria.toLowerCase();
        List<Job> result = new ArrayList<>();

        for (Job job : jobs) {
            if (job.getLocation() != null && job.getLocation().toLowerCase().contains(location)) {
                result.add(job);
            }
        }
        return result;
    }
}