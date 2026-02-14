package com.academo.util.mailservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class JavaMailApp implements IMailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendWelcomeMail(String destinatario) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(destinatario);
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
    public void sendActivationMail(String destinatario, String token){
        String baseUrl = System.getenv("CLIENT_URL");
        String urlDeAtivacao = baseUrl+"/auth/activate?value="+token;
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(destinatario);
        email.setSubject("Ative sua conta!");
        email.setText("""
                Olá,

                Ative sua conta para poder utilizar o Academo.
                Acesse o link abaixo para ativação:
                
                """ + urlDeAtivacao + " \n");

        email.setFrom("Equipe ACADEMO <" + System.getenv("MAIL_USERNAME").trim() + ">");
        mailSender.send(email);
    }
}
