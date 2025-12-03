// JobSearchStrategy.java
package com.jobsphere.strategy;

import com.jobsphere.model.Job;
import java.util.List;

/**
 * DESIGN PATTERN: Strategy Pattern
 * Why: Allows different search algorithms to be used interchangeably
 * Affected Classes: JobSearchStrategy, KeywordSearchStrategy, CategorySearchStrategy, LocationSearchStrategy
 */
public interface JobSearchStrategy {
    List<Job> search(List<Job> jobs, String criteria);
}