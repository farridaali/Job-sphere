package com.jobsphere.strategy;

import com.jobsphere.model.Job;
import java.util.List;
import java.util.stream.Collectors;

public class KeywordSearchStrategy implements JobSearchStrategy {
    @Override
    public List<Job> search(List<Job> jobs, String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return jobs.stream()
                .filter(job ->
                        job.getTitle().toLowerCase().contains(lowerKeyword) ||
                                job.getDescription().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());
    }
}