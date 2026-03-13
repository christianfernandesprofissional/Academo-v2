package com.academo.service.mail;

import com.academo.controller.dtos.activity.ActivityNotificationDTO;
import com.academo.controller.dtos.mail.ActivateAccountMailDTO;
import com.academo.controller.dtos.mail.WelcomeMailDTO;
import com.academo.controller.dtos.notification.NotificationDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Comparator;
import java.util.List;

//@Component
public class MailServiceImpl implements IMailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public MailServiceImpl(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendWelcomeMail(WelcomeMailDTO welcomeMailDTO) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(welcomeMailDTO.email());
        email.setSubject("Bem-vindo ao ACADEMO!");
        email.setText("""
                Olá,

                Sua conta foi ativada com sucesso!
                Agora você já pode acessar o sistema normalmente.

                """);
        email.setFrom("Equipe ACADEMO <" + System.getenv("MAIL_USERNAME").trim() + ">");

        mailSender.send(email);
    }

    @Override
    public void sendActivationMail(ActivateAccountMailDTO activateAccountMailDTO){
        String baseUrl = System.getenv("CLIENT_URL");
        String urlDeAtivacao = baseUrl+"/auth/activate?value="+activateAccountMailDTO.activationToken();
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(activateAccountMailDTO.email());
        email.setSubject("Ative sua conta!");
        email.setText("""
                Olá,

                Ative sua conta para poder utilizar o Academo.
                Acesse o link abaixo para ativação:
                
                """ + urlDeAtivacao + " \n");

        email.setFrom("Equipe ACADEMO <" + System.getenv("MAIL_USERNAME").trim() + ">");
        mailSender.send(email);
    }

    @Override
    public void sendEmails(List<NotificationDTO> notificationDTOList) throws MessagingException {
        for (NotificationDTO n : notificationDTOList){

            n.setActivityNotificationDTOS(
                    n.getActivityNotificationDTOS()
                            .stream()
                            .sorted(Comparator.comparing(ActivityNotificationDTO::activityDate))
                            .toList());

            Context context = new Context();
            context.setVariable("atividades", n.getActivityNotificationDTOS());

            String htmlContent = templateEngine.process("NotificationTemplate", context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setSubject("ACADEMO - Atividades Próximas");
            helper.setFrom("Equipe ACADEMO <" + System.getenv("MAIL_USERNAME").trim() + ">");
            helper.setTo(n.getEmail());
            helper.setText(htmlContent, true);

            mailSender.send(message);
        }
    }


}
