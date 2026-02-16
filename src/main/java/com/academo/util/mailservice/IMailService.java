package com.academo.util.mailservice;

public interface IMailService {

    public void sendWelcomeMail(String recipient);
    public void sendActivationMail(String recipient, String activationToken);
}
