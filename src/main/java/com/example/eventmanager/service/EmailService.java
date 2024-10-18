package com.example.eventmanager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EventService eventService;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendMessage(List<String> recipient, String body, String subject) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(fromEmail);
        mailMessage.setTo(recipient.toArray(new String[0]));
        mailMessage.setSubject(subject);
        mailMessage.setText(body);

        log.info("Sending message {}", subject);
        mailSender.send(mailMessage);

        // ДЛЯ ПРИМЕРА

//        String to = "f30j6au6ee@jxpomup.com";
//        String hostSMTP = "smtp.yandex.ru";
//        Integer port =  465;
//
//        Properties properties = new Properties();
//        properties.put("mail.smtp.host", hostSMTP);
//        properties.put("mail.smtp.ssl.enable", "true");
//        properties.put("mail.smtp.port", port);
//        properties.put("mail.smtp.auth", "true");
//
//        Session session = Session.getDefaultInstance(properties, new Authenticator() {
//            @Override
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication("nicita.gaidarenko@yandex.ru", "yxgsozkdoqsphgpx");
//            }
//        });
//
//        try {
//            Message message = new MimeMessage(session);
//            message.setFrom(new InternetAddress("nicita.gaidarenko@yandex.ru"));
//            InternetAddress[] addresses = {new InternetAddress(to)};
//            message.setRecipients(Message.RecipientType.TO, addresses);
//            message.setSubject("Email from java");
//            message.setSentDate(new Date());
//            message.setText("Exe work with Java mail");
//
//            Transport.send(message);
//            log.info("Message was sent: {}", message.getSubject());
//        } catch (Exception e) {
//            log.error("Can not to send message: {}", e.getMessage());
//        }
    }

    public void changingStatusEvent(Map<Long, List<String>> logins) {
        logins.forEach((eventId, userLogins) -> {
            String eventMessage = "Event %s already started".formatted(eventService.getEventById(eventId));
            sendMessage(userLogins, "Event Started", eventMessage);
        });
    }
}
