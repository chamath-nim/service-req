package com.mobitel.servicereq.job;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Component
public class subJob extends QuartzJobBean {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MailProperties mailProperties;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext){
        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();

        String subject = jobDataMap.getString("subject");
        String body = jobDataMap.getString("body");
        String recipientEmail = jobDataMap.getString("email");

        sendMail(mailProperties.getUsername(), recipientEmail, subject, body);
    }

    private void sendMail(String fromEmail, String toEmail, String subject, String body){
        System.out.println("Mail Send...");
        try {
            MimeMessage message = javaMailSender.createMimeMessage();

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, StandardCharsets.UTF_8.toString());
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(body, true);
            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(toEmail);
            javaMailSender.send(message);
        }
        catch (MessagingException e){
            System.out.println(e);
        }
    }
}
