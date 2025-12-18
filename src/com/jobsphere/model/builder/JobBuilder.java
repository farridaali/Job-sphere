package com.jobsphere.model.builder;

import java.util.List;

/**
 * dah el class ely by5alny n-decouple el creation bt3 el job
 *
 * by5alyna n3ml el object step by step we ne3ml validation 3al inputs bt3t job abl
 * man3ml assignment we dah bysa7l 3alna badel makol mara ne3ml job object n3ml instance
 * be constructor ad el gamosa
 */
public class JobBuilder {
    public Job job;


    public void createNewJob(Job job) {
        this.job = new Job(job.getDescription(), job.getTitle(), job.getCompanyEmail());
    }



    public void buildlocation(String location) {
        if (location != null && !location.trim().isEmpty()) {
            job.setLocation(location);
        }
    }

    public void buildType(String jobType) {
        if (jobType != null && !jobType.trim().isEmpty()) {
            job.setJobType(jobType);
        }
    }

    public void buildsalary(String salary) {
        if (salary != null && !salary.trim().isEmpty()) {
            job.setSalary(salary);
        }
    }

    public void buildrequirements(List<String> requirements) {
        if (requirements != null && !requirements.isEmpty()) {
            job.setRequirements(requirements);
        }
    }

    public void buildresponsibilities(List<String> responsibilities) {
        if (responsibilities != null && !responsibilities.isEmpty()) {
            job.setResponsibilities(responsibilities);
        }
    }

    public Job build() {
        return job;
    }

}