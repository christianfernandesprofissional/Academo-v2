package com.academo.service.mail;

import com.academo.controller.dtos.mail.ActivateAccountMailDTO;
import com.academo.controller.dtos.mail.WelcomeMailDTO;
import com.academo.controller.dtos.notification.NotificationDTO;
import jakarta.mail.MessagingException;

import java.util.List;

public interface IMailService {

    void sendWelcomeMail(WelcomeMailDTO welcomeMailDTO);
    void sendActivationMail(ActivateAccountMailDTO activateAccountMailDTO);
    void sendEmails(List<NotificationDTO> notificationDTOs) throws MessagingException;
}
