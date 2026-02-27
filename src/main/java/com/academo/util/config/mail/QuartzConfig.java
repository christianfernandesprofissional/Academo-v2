package com.academo.util.config.mail;

import com.academo.util.QuartzSchedule.notification.NotificationJob;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail notificationJobDetail(){
        return JobBuilder.newJob(NotificationJob.class)
                .withIdentity("notificationJob")
                .storeDurably()
                .build();
    }
}
