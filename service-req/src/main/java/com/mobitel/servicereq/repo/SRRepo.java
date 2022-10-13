package com.mobitel.servicereq.repo;

import com.mobitel.servicereq.model.SR;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SRRepo extends JpaRepository<SR, Long> {

    @Query("select s from SR s where (s.status = 'inprogress' and s.notifyCount < 3)")
    List<SR> findAllRequests();
}
