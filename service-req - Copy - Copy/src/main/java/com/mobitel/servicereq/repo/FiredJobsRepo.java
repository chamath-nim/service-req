package com.mobitel.servicereq.repo;

import com.mobitel.servicereq.model.JobDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FiredJobsRepo extends JpaRepository<JobDetails, Long> {

    @Query("select j from JobDetails j where j.notifyCount = ?1")
    List<JobDetails> getFiredJobs(int i);
}
