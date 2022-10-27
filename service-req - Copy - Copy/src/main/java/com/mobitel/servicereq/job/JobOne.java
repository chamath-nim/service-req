package com.mobitel.servicereq.job;

import com.mobitel.servicereq.model.JobDetails;
import com.mobitel.servicereq.model.SR;
import com.mobitel.servicereq.repo.FiredJobsRepo;
import com.mobitel.servicereq.repo.SRRepo;
import com.mobitel.servicereq.service.SRService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Slf4j
@Component
public class JobOne extends QuartzJobBean {

    @Autowired
    private SRRepo srRepo;
    @Autowired
    private FiredJobsRepo firedJobsRepo;

    Logger logger = LoggerFactory.getLogger(JobOne.class);

    @Override
    protected void executeInternal(JobExecutionContext context) {

        List<SR> requests = srRepo.findAllRequests1(0);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if (requests.size() == 0) logger.info("There is no in-progress requests in for job 1");
        else {
            for (SR sr : requests) {
                LocalDateTime dateTime = LocalDateTime.parse(sr.getRequestTime(), formatter);

                logger.info("job1 "+sr.getId());
                sr.setNotifyCount(1);
                srRepo.save(sr);

                JobDetails jobDetails = new JobDetails();
                jobDetails.setJobId(sr.getId());
                jobDetails.setNotifyCount(1);
                jobDetails.setRequestTime(sr.getRequestTime());
                jobDetails.setStatus("success");
                jobDetails.setFiredTime(CurrentDateTime());

                firedJobsRepo.save(jobDetails);
            }
        }
    }
    public String CurrentDateTime(){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return format.format(now);
    }
}
