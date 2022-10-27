package com.mobitel.servicereq.job;

import com.mobitel.servicereq.model.JobDetails;
import com.mobitel.servicereq.model.SR;
import com.mobitel.servicereq.repo.FiredJobsRepo;
import com.mobitel.servicereq.repo.SRRepo;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class JobTwo extends QuartzJobBean {

    @Autowired
    private SRRepo srRepo;
    @Autowired
    private FiredJobsRepo firedJobsRepo;
    @Autowired
    private Scheduler scheduler;

    Logger logger = LoggerFactory.getLogger(JobTwo.class);

    @Override
    protected void executeInternal(JobExecutionContext context) {

        List<SR> requests = new ArrayList<>();

        try {
            Trigger.TriggerState state = Trigger.TriggerState.NORMAL;
            for (TriggerKey triggerKey : scheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals("group1"))) {
                state = scheduler.getTriggerState(triggerKey);
            }
            if (state.equals(Trigger.TriggerState.PAUSED)){
                List<SR> req1 = srRepo.findAllRequests1(0);
                List<SR> req2 = srRepo.findAllRequests2();

                requests.addAll(req1);
                requests.addAll(req2);
            }
            else requests = srRepo.findAllRequests2();

        } catch (SchedulerException e) {
            logger.error(String.valueOf(e));
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ZoneId zoneId = ZoneId.systemDefault();

        if (requests.size() == 0) logger.info("There is no in-progress requests in for job 2");
        else {
            for (SR sr : requests) {
                LocalDateTime dateTime = LocalDateTime.parse(sr.getRequestTime(), formatter);

                LocalDateTime nextDateTime = dateTime.plusMinutes(3);
                ZonedDateTime zonedDateTime = nextDateTime.atZone(zoneId);

                ZonedDateTime checking = ZonedDateTime.now();

                if (checking.compareTo(zonedDateTime) > 0) {
                    logger.info("job2 "+sr.getId());
                    sr.setNotifyCount(2);
                    srRepo.save(sr);

                    JobDetails jobDetails = new JobDetails();
                    jobDetails.setJobId(sr.getId());
                    jobDetails.setNotifyCount(2);
                    jobDetails.setRequestTime(sr.getRequestTime());
                    jobDetails.setStatus("success");
                    jobDetails.setFiredTime(CurrentDateTime());

                    firedJobsRepo.save(jobDetails);
                }
            }
        }
    }
    public String CurrentDateTime(){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return format.format(now);
    }
}
