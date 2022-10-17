package com.mobitel.servicereq.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class subJob extends QuartzJobBean {

    Logger logger = LoggerFactory.getLogger(MainJob.class);

    @Override
    protected void executeInternal(JobExecutionContext context) {
        JobDataMap jobDataMap = context.getMergedJobDataMap();

        Long id = jobDataMap.getLong("id");
        int name = jobDataMap.getInt("name");
        String group = jobDataMap.getString("group");

        logger.info(id+"-"+name+"-"+group+ " job done");
    }
}
