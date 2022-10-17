package com.mobitel.servicereq.service;

import com.mobitel.servicereq.job.MainJob;
import com.mobitel.servicereq.job.subJob;
import com.mobitel.servicereq.model.SR;
import com.mobitel.servicereq.repo.SRRepo;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

@Service
@Slf4j
public class SRService {

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private SRRepo srRepo;

    Logger logger = LoggerFactory.getLogger(SRService.class);

    public void mainTask() {
        try {
            JobDetail job = newJob(MainJob.class)
                    .withIdentity(UUID.randomUUID().toString(), "group1")
                    .build();

            Trigger trigger = newTrigger()
                    .withIdentity(job.getKey().getName(), "group1")
                    .withSchedule(cronSchedule(" */30 * * * * ?"))
                    .forJob(job)
                    .build();

            scheduler.scheduleJob(job,trigger);
        }
        catch (SchedulerException e){
            logger.error(String.valueOf(e));
        }
    }
    public void endMainJob(String jobGroup){
        System.out.println(jobGroup);
        try {
            for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(jobGroup))) {
                System.out.println(jobKey.getName());
                scheduler.deleteJob(jobKey);
            }
        }
        catch (SchedulerException e){
            logger.error(String.valueOf(e));
        }
    }

    public void addRequst(SR sr){
        sr.setRequestTime(CurrentDateTime());
        srRepo.save(sr);
    }

    public String CurrentDateTime(){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return format.format(now);
    }

    public void subJobExecution(){
        List<SR> requests = srRepo.findAllRequests();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ZoneId zoneId = ZoneId.systemDefault();
        if (requests.size() == 0) logger.info("There is no in-progress requests");
        else {
            for(SR sr : requests){
                if (sr.getNotifyCount() == 0){
                    LocalDateTime dateTime = LocalDateTime.parse(sr.getRequestTime(), formatter);
//                    System.out.println(sr.getId().toString() +"  "+ dateTime+" "+0);

                    try {
                        JobDataMap jobDataMap = new JobDataMap();

                        jobDataMap.put("id", sr.getId());
                        jobDataMap.put("name", 1);
                        jobDataMap.put("group", "email-1 trigger");
                        jobDataMap.put("date", dateTime);


                        JobDetail job1 = newJob(subJob.class)
                                .withIdentity("1", "job1")
                                .usingJobData(jobDataMap)
                                .build();

                        Trigger trigger1 = TriggerBuilder.newTrigger()
                                .forJob(job1)
                                .withIdentity(job1.getKey().getName(), "email-1 trigger")
                                .withDescription("send email -")
                                .startNow()
                                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                                .build();


                        scheduler.scheduleJob(job1, trigger1);
                        sr.setNotifyCount(sr.getNotifyCount()+1);
                        srRepo.save(sr);
                    }
                    catch (SchedulerException e) {
                        logger.error(String.valueOf(e));
                    }

                }
                else if (sr.getNotifyCount() == 1){
                    LocalDateTime dateTime = LocalDateTime.parse(sr.getRequestTime(), formatter);

                    LocalDateTime nextDateTime = dateTime.plusMinutes(3);
                    ZonedDateTime zonedDateTime = nextDateTime.atZone( zoneId );

                    ZonedDateTime checking = ZonedDateTime.now();

//                    System.out.println(sr.getId().toString() +"  "+ dateTime+" "+1+" "+zonedDateTime);

                    if (checking.compareTo(zonedDateTime) > 0) {
                        try {
                            JobDataMap jobDataMap = new JobDataMap();

                            jobDataMap.put("id", sr.getId());
                            jobDataMap.put("name", 2);
                            jobDataMap.put("group", "email-2 trigger");


                            JobDetail job2 = newJob(subJob.class)
                                    .withIdentity("2", "job2")
                                    .usingJobData(jobDataMap)
                                    .build();

                            Trigger trigger2 = TriggerBuilder.newTrigger()
                                    .forJob(job2)
                                    .withIdentity(job2.getKey().getName(), "email-2 trigger")
                                    .withDescription("send email -")
                                    .startNow()
                                    .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                                    .build();


                            scheduler.scheduleJob(job2, trigger2);
                            sr.setNotifyCount(sr.getNotifyCount()+1);
                            srRepo.save(sr);

                        } catch (SchedulerException e) {
                            logger.error(String.valueOf(e));
                        }
                    }
                }
                else if (sr.getNotifyCount() == 2){
                    LocalDateTime dateTime = LocalDateTime.parse(sr.getRequestTime(), formatter);

                    LocalDateTime nextDateTime = dateTime.plusMinutes(5);
                    ZonedDateTime zonedDateTime = nextDateTime.atZone( zoneId );

                    ZonedDateTime checking = ZonedDateTime.now();

//                    System.out.println(sr.getId().toString() +"  "+ dateTime+" "+2+" "+zonedDateTime);

                    if (checking.compareTo(zonedDateTime) > 0) {
                        try {
                            JobDataMap jobDataMap = new JobDataMap();

                            jobDataMap.put("id", sr.getId());
                            jobDataMap.put("name", 3);
                            jobDataMap.put("group", "email-3 trigger");


                            JobDetail job3 = newJob(subJob.class)
                                    .withIdentity("3", "job3")
                                    .usingJobData(jobDataMap)
                                    .build();

                            Trigger trigger3 = TriggerBuilder.newTrigger()
                                    .forJob(job3)
                                    .withIdentity(job3.getKey().getName(), "email-3 trigger")
                                    .withDescription("send email -")
                                    .startNow()
                                    .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                                    .build();


                            scheduler.scheduleJob(job3, trigger3);
                            sr.setNotifyCount(sr.getNotifyCount()+1);
                            srRepo.save(sr);

                        } catch (SchedulerException e) {
                            logger.error(String.valueOf(e));
                        }
                    }
                }
            }

        }
    }
}
