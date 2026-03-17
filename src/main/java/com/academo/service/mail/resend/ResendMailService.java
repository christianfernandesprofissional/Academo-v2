package com.academo.service.mail.resend;

import com.academo.controller.dtos.mail.ActivateAccountMailDTO;
import com.academo.controller.dtos.mail.WelcomeMailDTO;
import com.academo.controller.dtos.notification.NotificationDTO;
import com.academo.service.mail.IMailService;
import com.academo.util.exceptions.mail.MailException;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.Template;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ResendMailService implements IMailService {

    private String resendAPIKey;
    private final String academoMail;

    private final Resend resend;

    public ResendMailService(@Value("${resend.service.api.key}") String resendAPIKey, @Value("${resend.academo.mail}") String academoMail) {
        this.resend = new Resend(resendAPIKey);
        this.academoMail = academoMail;
    }

    @Async
    @Override
    public void sendWelcomeMail(WelcomeMailDTO welcomeMailDTO) {

        Map<String, Object> variables = new HashMap<>();
        variables.put("user_email", welcomeMailDTO.email());
        variables.put("name", welcomeMailDTO.name());

        try {
            CreateEmailOptions params = CreateEmailOptions.builder()
                    .to(welcomeMailDTO.email())
                    .template(Template.builder()
                            .id("boas-vindas")
                            .variables(variables)
                            .build())
                    .build();

            resend.emails().send(params);
        } catch (ResendException e) {
            throw new MailException("Erro ao enviar email de Boas-Vindas");
        }
    }

    @Async
    @Override
    public void sendActivationMail(ActivateAccountMailDTO activateAccountMailDTO) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("user_email", activateAccountMailDTO.email());
        variables.put("name", activateAccountMailDTO.name());
        variables.put("activationToken", activateAccountMailDTO.activationToken());

        CreateEmailOptions params = CreateEmailOptions.builder()
                .to(activateAccountMailDTO.email())
                .template(Template.builder()
                        .id("ativar-conta")
                        .variables(variables)
                        .build())
                .build();
        try {
            resend.emails().send(params);
        } catch (ResendException e) {
            throw new RuntimeException("Erro ao enviar email de Ativação de Conta");
        }
    }

    @Async
    @Override
    public void sendEmails(List<NotificationDTO> notificationDTOs) throws MessagingException {

    }
}
