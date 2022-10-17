package com.mobitel.servicereq.job;

import com.mobitel.servicereq.service.SRService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class MainJob extends QuartzJobBean {

    @Autowired
    private SRService srService;

    Logger logger = LoggerFactory.getLogger(MainJob.class);

    @Override
    protected void executeInternal(JobExecutionContext context) {
        srService.subJobExecution();
        logger.info("working well");

    }
}
