package com.acledabank.vicheak.api.core.mail;


import jakarta.mail.MessagingException;

public interface MailService {

    void sendMail(Mail<?> mail) throws MessagingException;

}
