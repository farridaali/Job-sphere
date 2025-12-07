package com.jobsphere.search;

import com.jobsphere.model.Job;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Search by location
 */
public class LocationSearchStrategy implements SearchStrategy {
    @Override
    public List<Job> search(List<Job> jobs, String criteria) {
        String location = criteria.toLowerCase();
        return jobs.stream()
                .filter(job -> job.getLocation() != null &&
                        job.getLocation().toLowerCase().contains(location))
                .collect(Collectors.toList());
    }
}