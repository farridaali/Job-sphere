package com.jobsphere.strategy;

import com.jobsphere.model.Job;
import java.util.List;
import java.util.stream.Collectors;

public class LocationSearchStrategy implements JobSearchStrategy {
    @Override
    public List<Job> search(List<Job> jobs, String location) {
        String lowerLocation = location.toLowerCase();
        return jobs.stream()
                .filter(job -> job.getLocation() != null &&
                        job.getLocation().toLowerCase().contains(lowerLocation))
                .collect(Collectors.toList());
    }
}