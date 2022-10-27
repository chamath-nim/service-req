package com.mobitel.servicereq;

import com.mobitel.servicereq.service.SRService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ServiceReqApplication {

    public static void main(String[] args) {
        ApplicationContext context =  SpringApplication.run(ServiceReqApplication.class, args);

        SRService srService = context.getBean(SRService.class);
        srService.mainTask();
    }

}
