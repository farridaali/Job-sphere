package com.jobsphere.model.builder;

import java.util.List;

public  class JobBuilder {
    public Job job;


    public void createNewJob(Job job) {
       this. job = new Job(job.getDescription(),job.getTitle(),job.getCompanyEmail());
    }


    public  void buildlocation(String location) {
        if (location != null)
       job.setLocation(location);
    }

    public void buildType(String jobType) {
        if (jobType != null)
        job.setJobType(jobType);
    }

    public void buildsalary(String salary) {
        if (salary != null)
        job.setSalary(salary);
    }

    public void buildrequirements(List<String> requirements) {
        if (requirements != null)
        job.setRequirements(requirements);
    }

    public void buildresponsibilities(List<String> responsibilities) {
        if (responsibilities != null)
       job.setResponsibilities(responsibilities);
    }

    public void buildstatus(Job.JobStatus status) {
        if (status != null)
      job.setStatus(status);
    }

    public Job build() {
        return job;
    }
}

