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
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class JobThree extends QuartzJobBean{

    @Autowired
    private SRRepo srRepo;
    @Autowired
    private FiredJobsRepo firedJobsRepo;
    @Autowired
    private Scheduler scheduler;

    Logger logger = LoggerFactory.getLogger(JobThree.class);

    @Override
    protected void executeInternal(JobExecutionContext context) {
        List<SR> requests = new ArrayList<>();

        try {
            Trigger.TriggerState state1 = Trigger.TriggerState.NORMAL;
            Trigger.TriggerState state2 = Trigger.TriggerState.NORMAL;

            for (TriggerKey triggerKey : scheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals("group1"))) {
                state1 = scheduler.getTriggerState(triggerKey);
            }
            for (TriggerKey triggerKey : scheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals("group2"))) {
                state2 = scheduler.getTriggerState(triggerKey);
            }

            if (state1.equals(Trigger.TriggerState.PAUSED) && state2.equals(Trigger.TriggerState.PAUSED)){
                requests.addAll(srRepo.findAllRequests1(0));
                requests.addAll(srRepo.findAllRequests2());
                requests.addAll(srRepo.findAllRequests3());
            }
            else if (state1.equals(Trigger.TriggerState.NORMAL) && state2.equals(Trigger.TriggerState.PAUSED)){
                requests.addAll(srRepo.findAllRequests2());
                requests.addAll(srRepo.findAllRequests3());
            }
            else requests = srRepo.findAllRequests3();

        } catch (SchedulerException e) {
            logger.error(String.valueOf(e));
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ZoneId zoneId = ZoneId.systemDefault();

        if (requests.size() == 0) logger.info("There is no in-progress requests in for job 3");
        else {
            for (SR sr : requests) {
                LocalDateTime dateTime = LocalDateTime.parse(sr.getRequestTime(), formatter);

                LocalDateTime nextDateTime = dateTime.plusMinutes(5);
                ZonedDateTime zonedDateTime = nextDateTime.atZone(zoneId);

                ZonedDateTime checking = ZonedDateTime.now();

                if (checking.compareTo(zonedDateTime) > 0) {
                    logger.info("job3 "+sr.getId());
                    sr.setNotifyCount(3);
                    srRepo.save(sr);

                    JobDetails jobDetails = new JobDetails();
                    jobDetails.setJobId(sr.getId());
                    jobDetails.setNotifyCount(3);
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
