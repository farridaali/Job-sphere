package com.jobsphere.search;

import com.jobsphere.model.Job;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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