package com.academo.service.mail;

import com.academo.controller.dtos.notification.NotificationDTO;
import jakarta.mail.MessagingException;

import java.util.List;

public interface IMailService {

    void sendWelcomeMail(String recipient);
    void sendActivationMail(String recipient, String activationToken);
    void sendEmails(List<NotificationDTO> notificationDTOs) throws MessagingException;
}
