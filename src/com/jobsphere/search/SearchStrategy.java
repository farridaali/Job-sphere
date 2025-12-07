package com.jobsphere.search;

import com.jobsphere.model.Job;
import java.util.*;
import java.util.stream.Collectors;

/**
 * STRATEGY PATTERN
 * Why: Different search algorithms for jobs (keyword, location, type)
 * Affected: Job search functionality in JobSearchPanel
 * Benefit: Easy to add new search strategies, interchangeable algorithms
 */
public interface SearchStrategy {
    List<Job> search(List<Job> jobs, String criteria);
}







