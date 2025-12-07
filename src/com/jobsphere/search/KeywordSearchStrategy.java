package com.jobsphere.search;

import com.jobsphere.model.Job;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Search by keyword in title or description
 */
public class KeywordSearchStrategy implements SearchStrategy {
    @Override
    public List<Job> search(List<Job> jobs, String criteria) {
        String keyword = criteria.toLowerCase();
        return jobs.stream()
                .filter(job -> job.getTitle().toLowerCase().contains(keyword) ||
                        (job.getDescription() != null && job.getDescription().toLowerCase().contains(keyword)))
                .collect(Collectors.toList());
    }
}