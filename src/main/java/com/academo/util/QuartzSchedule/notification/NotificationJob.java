package com.academo.util.QuartzSchedule.notification;

import com.academo.repository.ActivityRepository;
import com.academo.service.mail.IMailService;
import jakarta.mail.MessagingException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.time.LocalDate;

//@Component
public class NotificationJob implements Job {

    private final IMailService mailService;
    private final ActivityRepository activityRepository;

    public NotificationJob(IMailService mailService, ActivityRepository activityRepository) {
        this.mailService = mailService;
        this.activityRepository = activityRepository;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            mailService.sendEmails(activityRepository.searchNotificationByDate(LocalDate.now()));
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
