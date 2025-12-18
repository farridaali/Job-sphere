package com.jobsphere.search;

import com.jobsphere.model.builder.Job;

import java.util.ArrayList;
import java.util.List;


public class KeywordSearchStrategy implements SearchStrategy {
    @Override
    public List<Job> search(List<Job> jobs, String criteria) {
        String keyword = criteria.toLowerCase();
        List<Job> result = new ArrayList<>();
        for (Job job : jobs) {
            boolean titleMatches = job.getTitle() != null && job.getTitle().toLowerCase().contains(keyword);
            boolean descriptionMatches = job.getDescription() != null && job.getDescription().toLowerCase().contains(keyword);

            if (titleMatches || descriptionMatches) {
                result.add(job);}
        }
        return result;
    }
}